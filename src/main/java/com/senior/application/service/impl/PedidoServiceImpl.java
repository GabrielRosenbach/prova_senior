package com.senior.application.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.senior.application.constants.MensagemServidor;
import com.senior.application.enums.SituacaoPedidoEnum;
import com.senior.application.enums.TipoProdutoServicoEnum;
import com.senior.application.facade.PedidoFacade;
import com.senior.application.model.ItemPedido;
import com.senior.application.model.Pedido;
import com.senior.application.model.ProdutoServico;
import com.senior.application.repository.PedidoRepository;
import com.senior.application.service.ItemPedidoService;
import com.senior.application.service.PedidoService;
import com.senior.application.util.IntegerUtil;
import com.senior.application.util.OptionalUtil;
import com.senior.prova.application.dto.CadastroItemPedidoDTO;
import com.senior.prova.application.dto.CadastroPedidoDTO;

import jakarta.transaction.Transactional;

@Service
public class PedidoServiceImpl implements PedidoService {

	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private ItemPedidoService itemPedidoService;

	@Override
	@Transactional
	public Pedido createPedido(CadastroPedidoDTO cadastroPedidoDTO) {

		Pedido pedido = new Pedido();
		pedido.setDesconto(cadastroPedidoDTO.getDesconto().floatValue());
		pedido.setSituacao(
				IntegerUtil.isNullOrZero(cadastroPedidoDTO.getSituacao()) ? SituacaoPedidoEnum.ABERTO.getCodigo()
						: cadastroPedidoDTO.getSituacao());
		pedido.setItens(gerarItemPedido(null, pedido, cadastroPedidoDTO.getItens()));
		pedido.setTotal(calcularTotal(pedido));

		return pedidoRepository.save(pedido);
	}

	private Float calcularTotal(Pedido pedido) {
		Float totalProduto = 0f;
		Float totalServico = 0f;

		for (ItemPedido itemPedido : pedido.getItens()) {
			ProdutoServico produtoServico = itemPedido.getProdutoServico();

			if (produtoServico.getTipo().equals(TipoProdutoServicoEnum.PRODUTO.getCodigo())) {
				totalProduto += produtoServico.getPreco() * itemPedido.getQuantidade();
			} else {
				totalServico += produtoServico.getPreco();
			}
		}

		return totalServico + totalProduto * (1 - pedido.getDesconto() / 100);
	}

	private List<ItemPedido> gerarItemPedido(UUID idPedido, Pedido pedido, List<CadastroItemPedidoDTO> listItemDTO) {
		return listItemDTO.stream()
				.map(itemDTO -> itemPedidoService.validarItem(Optional.ofNullable(idPedido),
						Optional.ofNullable(pedido), UUID.fromString(itemDTO.getIdProdutoServico()),
						itemDTO.getQuantidade()))
				.toList();
	}

	@Override
	public void deletePedido(UUID id) {
		Pedido pedido = readPedido(id);
		PedidoFacade.validarSituacaoFechada(pedido);
		pedidoRepository.deleteById(id);
	}

	@Override
	public List<Pedido> listPedido(Integer inicio, Integer tamanho, Boolean ascendente, String campoOrderBy) {
		return pedidoRepository
				.findAll(PageRequest.of(inicio, tamanho, ascendente ? Direction.ASC : Direction.DESC, campoOrderBy))
				.toList();
	}

	@Override
	public Pedido readPedido(UUID id) {
		return OptionalUtil.tratarOptional(pedidoRepository.findById(id), MensagemServidor.PEDIDO_NAO_ENCONTRADO);
	}

	@Override
	public Pedido updatePedido(UUID id, CadastroPedidoDTO cadastroPedidoDTO) {
		Pedido pedido = readPedido(id);

		PedidoFacade.validarSituacaoFechada(pedido);
		pedido.setSituacao(cadastroPedidoDTO.getSituacao());

		pedido.setDesconto(cadastroPedidoDTO.getDesconto().floatValue());
		pedido.setItens(gerarItemPedido(id, pedido, cadastroPedidoDTO.getItens()));
		pedido.setTotal(calcularTotal(pedido));

		return pedidoRepository.save(pedido);
	}

	@Override
	public void atualizarTotal(UUID id) {
		Pedido pedido = readPedido(id);
		pedido.setTotal(calcularTotal(pedido));
		pedidoRepository.save(pedido);
	}

}
