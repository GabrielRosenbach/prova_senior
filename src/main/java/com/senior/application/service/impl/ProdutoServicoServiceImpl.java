package com.senior.application.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.senior.application.constants.MensagemServidor;
import com.senior.application.exceptions.http.InternalErrorException;
import com.senior.application.model.ItemPedido;
import com.senior.application.model.ProdutoServico;
import com.senior.application.repository.ItemPedidoRepository;
import com.senior.application.repository.ProdutoServicoRepository;
import com.senior.application.service.ProdutoServicoService;
import com.senior.application.util.OptionalUtil;
import com.senior.prova.application.dto.CadastroProdutoServicoDTO;

@Service
public class ProdutoServicoServiceImpl implements ProdutoServicoService {

	@Autowired
	private ProdutoServicoRepository produtoServicoRepository;

	@Autowired
	private ItemPedidoRepository itemPedidoRepository;

	@Override
	@Transactional
	public ProdutoServico createProdutoServico(CadastroProdutoServicoDTO cadastroProdutoServicoDTO) {

		ProdutoServico produtoServico = new ProdutoServico(cadastroProdutoServicoDTO);

		return produtoServicoRepository.save(produtoServico);
	}

	@Override
	public ProdutoServico readProdutoServico(UUID id) {
		return OptionalUtil.tratarOptional(produtoServicoRepository.findById(id),
				MensagemServidor.PRODUTO_SERVICO_NAO_ENCONTRADO);
	}

	@Override
	public ProdutoServico updateProdutoServico(UUID id, CadastroProdutoServicoDTO cadastroProdutoServicoDTO) {

		ProdutoServico produtoServico = readProdutoServico(id);

		produtoServico.setDescricao(cadastroProdutoServicoDTO.getDescricao());
		produtoServico.setTipo(cadastroProdutoServicoDTO.getTipo());
		produtoServico.setSituacao(cadastroProdutoServicoDTO.getSituacao());

		return produtoServicoRepository.save(produtoServico);
	}

	@Override
	public void deleteProdutoServico(UUID id) {
		// Lança NotFoundException caso o id não exista
		readProdutoServico(id);
		
		ItemPedido itemPedido = new ItemPedido();
		itemPedido.getItemPedidoId().setIdProdutoServico(id);
		Example<ItemPedido> example = Example.of(itemPedido, ExampleMatcher.matching());
		
		if (itemPedidoRepository.count(example) > 0) {
			throw new InternalErrorException(MensagemServidor.EXCLUIR_PRODUTO_SERVICO_VINCULADO_PEDIDO);
		}
		produtoServicoRepository.deleteById(id);
	}

	@Override
	public List<ProdutoServico> listProdutoServico(Integer inicio, Integer tamanho, Boolean ascendente,
			String campoOrderBy) {
		return produtoServicoRepository
				.findAll(PageRequest.of(inicio, tamanho, ascendente ? Direction.ASC : Direction.DESC, campoOrderBy))
				.toList();
	}

	@Override
	public Boolean exists(UUID idProdutoServico) {
		return produtoServicoRepository.existsById(idProdutoServico);
	}
}
