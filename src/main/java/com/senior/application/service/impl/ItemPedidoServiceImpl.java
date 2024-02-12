package com.senior.application.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.senior.application.constants.MensagemServidor;
import com.senior.application.enums.SituacaoProdutoServicoEnum;
import com.senior.application.enums.TipoProdutoServicoEnum;
import com.senior.application.exceptions.http.InternalErrorException;
import com.senior.application.facade.PedidoFacade;
import com.senior.application.model.ItemPedido;
import com.senior.application.model.Pedido;
import com.senior.application.model.ProdutoServico;
import com.senior.application.model.id.ItemPedidoId;
import com.senior.application.repository.ItemPedidoRepository;
import com.senior.application.repository.PedidoRepository;
import com.senior.application.service.ItemPedidoService;
import com.senior.application.service.ProdutoServicoService;
import com.senior.application.util.IntegerUtil;
import com.senior.application.util.OptionalUtil;
import com.senior.prova.application.dto.CadastroItemPedidoDTO;

@Service
public class ItemPedidoServiceImpl implements ItemPedidoService {

	@Autowired
	private ItemPedidoRepository itemPedidoRepository;

	@Autowired
	private ProdutoServicoService produtoServicoService;

	@Autowired
	private PedidoRepository pedidoRepository;

	@Override
	public ItemPedido createItemPedido(UUID idPedido, CadastroItemPedidoDTO cadastroItemPedidoDTO) {
		Pedido pedido = OptionalUtil.tratarOptional(pedidoRepository.findById(idPedido),
				MensagemServidor.PEDIDO_NAO_ENCONTRADO);

		PedidoFacade.validarSituacaoFechada(pedido);
		return itemPedidoRepository.save(validarItem(Optional.ofNullable(idPedido), Optional.ofNullable(pedido),
				UUID.fromString(cadastroItemPedidoDTO.getIdProdutoServico()), cadastroItemPedidoDTO.getQuantidade()));
	}

	@Override
	public void deleteItemPedido(UUID idPedido, UUID idProdutoServico) {
		Pedido pedido = OptionalUtil.tratarOptional(pedidoRepository.findById(idPedido),
				MensagemServidor.PEDIDO_NAO_ENCONTRADO);

		PedidoFacade.validarSituacaoFechada(pedido);
		pedido.getItens().removeIf(item -> item.getItemPedidoId().getIdProdutoServico().equals(idProdutoServico));
		pedidoRepository.save(pedido);
	}

	@Override
	public ItemPedido readItemPedido(UUID idPedido, UUID idProdutoServico) {
		return OptionalUtil.tratarOptional(itemPedidoRepository.findById(new ItemPedidoId(idPedido, idProdutoServico)),
				MensagemServidor.ITEM_PEDIDO_NAO_ENCONTRADO);
	}

	@Override
	public List<ItemPedido> listItensPedidos(Integer inicio, Integer tamanho, Boolean ascendente, String campoOrderBy) {
		campoOrderBy = validarCampoOrderBy(campoOrderBy);
		return itemPedidoRepository
				.findAll(PageRequest.of(inicio, tamanho, ascendente ? Direction.ASC : Direction.DESC, campoOrderBy))
				.toList();
	}
	
	private String validarCampoOrderBy(String campoOrderBy) {
		if ((campoOrderBy.contains("idPedido") || campoOrderBy.contains("idProdutoServico"))
				&& !campoOrderBy.contains("itemPedidoId")) {
			return "itemPedidoId." + campoOrderBy;
		}
		
		return campoOrderBy;
	}

	@Override
	public List<ItemPedido> listItensPedido(UUID idPedido, Integer inicio, Integer tamanho, Boolean ascendente,
			String campoOrderBy) {

		campoOrderBy = validarCampoOrderBy(campoOrderBy);

		ItemPedido itemPedido = new ItemPedido();
		itemPedido.getItemPedidoId().setIdPedido(idPedido);

		Example<ItemPedido> example = Example.of(itemPedido, ExampleMatcher.matching());
		return itemPedidoRepository
				.findAll(example,
						PageRequest.of(inicio, tamanho, ascendente ? Direction.ASC : Direction.DESC, campoOrderBy))
				.toList();
	}

	@Override
	public List<ItemPedido> updateItensPedido(UUID idPedido, List<CadastroItemPedidoDTO> listCadastroItemPedidoDTO) {
		List<ItemPedido> listItemPedido = listCadastroItemPedidoDTO.stream()
				.map(cadastroItemDTO -> validarItem(Optional.ofNullable(idPedido), Optional.empty(),
						UUID.fromString(cadastroItemDTO.getIdProdutoServico()), cadastroItemDTO.getQuantidade()))
				.collect(Collectors.toList());

		return itemPedidoRepository.saveAll(listItemPedido);
	}

	@Override
	public ItemPedido validarItem(Optional<UUID> optIdPedido, Optional<Pedido> optPedido, UUID idProdutoServico,
			Integer quantidade) {

		ProdutoServico produtoServico = produtoServicoService.readProdutoServico(idProdutoServico);
		
		if (produtoServico.getSituacao().equals(SituacaoProdutoServicoEnum.DESATIVADO.getCodigo())) {
			throw new InternalErrorException(MensagemServidor.ADICIONAR_ITEM_PRODUTO_SERVICO_DESATIVADO);
		}

		Boolean isProduto = TipoProdutoServicoEnum.PRODUTO.getCodigo().equals(produtoServico.getTipo());
		Boolean semQuantidade = IntegerUtil.isNullOrZero(quantidade);

		if ((isProduto && semQuantidade) || !(isProduto || semQuantidade)) {
			throw new InternalErrorException(MensagemServidor.QUANTIDADE_PRODUTO_NAO_INFORMADO);
		}

		if (optIdPedido.isEmpty() && optPedido.isEmpty()) {
			throw new InternalErrorException(MensagemServidor.NAO_INFORMADO_PEDIDO_PARA_ITEM);
		}

		ItemPedido itemPedido = new ItemPedido();
		Optional<ItemPedido> optItemPedido = Optional.empty();
		if (optIdPedido.isPresent()) {
			UUID idPedido = optIdPedido.get();
			OptionalUtil.tratarOptional(pedidoRepository.findById(idPedido), MensagemServidor.PEDIDO_NAO_ENCONTRADO);
			optItemPedido = itemPedidoRepository.findById(new ItemPedidoId(idPedido, idProdutoServico));
		}

		if (optItemPedido.isEmpty()) {
			if (optPedido.isEmpty()) {
				throw new InternalErrorException(MensagemServidor.NAO_INFORMADO_PEDIDO_PARA_ITEM);
			}

			PedidoFacade.validarSituacaoFechada(optPedido.get());
			itemPedido.setPedido(optPedido.get());
			itemPedido.setProdutoServico(produtoServico);
		} else {
			itemPedido = optItemPedido.get();
			PedidoFacade.validarSituacaoFechada(itemPedido.getPedido());
		}

		itemPedido.setQuantidade(isProduto ? quantidade : null);

		return itemPedido;
	}
}
