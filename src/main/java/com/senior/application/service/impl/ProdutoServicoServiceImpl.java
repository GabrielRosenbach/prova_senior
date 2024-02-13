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
import com.senior.application.exceptions.http.NotFoundException;
import com.senior.application.model.ItemPedido;
import com.senior.application.model.ProdutoServico;
import com.senior.application.repository.ItemPedidoRepository;
import com.senior.application.repository.ProdutoServicoRepository;
import com.senior.application.service.ProdutoServicoService;
import com.senior.application.util.OptionalUtil;
import com.senior.prova.application.dto.CadastroProdutoServicoDTO;

/**
 * Classe de serviço de Produto/Serviço
 */
@Service
public class ProdutoServicoServiceImpl implements ProdutoServicoService {

	@Autowired
	private ProdutoServicoRepository produtoServicoRepository;

	@Autowired
	private ItemPedidoRepository itemPedidoRepository;

	/**
	 * Cria um produto/Serviço
	 * 
	 * @param cadastroProdutoServicoDTO Objeto de cadastro de um produto/serviço
	 * @return ProdutoServico referente
	 */
	@Override
	@Transactional
	public ProdutoServico createProdutoServico(CadastroProdutoServicoDTO cadastroProdutoServicoDTO) {

		ProdutoServico produtoServico = new ProdutoServico(cadastroProdutoServicoDTO);

		return produtoServicoRepository.save(produtoServico);
	}

	/**
	 * Busca um produto/serviço
	 * 
	 * @param id UUID do produto/serviço
	 * @return ProdutoServico refernte
	 * @exception NotFoundException caso o pedido não for encontrado na base.
	 */
	@Override
	public ProdutoServico readProdutoServico(UUID id) {
		return OptionalUtil.tratarOptional(produtoServicoRepository.findById(id),
				MensagemServidor.PRODUTO_SERVICO_NAO_ENCONTRADO);
	}

	/**
	 * Atualiza um produto/serviço
	 * 
	 * @param id                        UUID do produto/serviço
	 * @param cadastroProdutoServicoDTO Objeto de cadastro de produto/serviço
	 * @return ProdutoServico referente
	 */
	@Override
	public ProdutoServico updateProdutoServico(UUID id, CadastroProdutoServicoDTO cadastroProdutoServicoDTO) {

		ProdutoServico produtoServico = readProdutoServico(id);

		produtoServico.setDescricao(cadastroProdutoServicoDTO.getDescricao());
		produtoServico.setPreco(cadastroProdutoServicoDTO.getPreco());
		produtoServico.setTipo(cadastroProdutoServicoDTO.getTipo());
		produtoServico.setSituacao(cadastroProdutoServicoDTO.getSituacao());

		return produtoServicoRepository.save(produtoServico);
	}

	/**
	 * Exclui um produto/serviço
	 * @param id UUID do produto/serviço
	 * @exception InternalErrorException caso o produto estiver vinculado em pedido
	 */
	@Override
	public void deleteProdutoServico(UUID id) {
		readProdutoServico(id);

		ItemPedido itemPedido = new ItemPedido();
		itemPedido.getItemPedidoId().setIdProdutoServico(id);
		Example<ItemPedido> example = Example.of(itemPedido, ExampleMatcher.matching());

		if (itemPedidoRepository.count(example) > 0) {
			throw new InternalErrorException(MensagemServidor.EXCLUIR_PRODUTO_SERVICO_VINCULADO_PEDIDO);
		}
		produtoServicoRepository.deleteById(id);
	}

	/**
	 * Lista os produtos/serviços cadastrados
	 * 
	 * @param inicio       Inicio da paginação
	 * @param tamanho      Tamanho da paginação
	 * @param ascendente   Boolean se busca é ascendente ou descendente
	 * @param campoOrderBy campo que será considerado para a ordenação
	 * @return Lista de ProdutoServico
	 */
	@Override
	public List<ProdutoServico> listProdutoServico(Integer inicio, Integer tamanho, Boolean ascendente,
			String campoOrderBy) {
		return produtoServicoRepository
				.findAll(PageRequest.of(inicio, tamanho, ascendente ? Direction.ASC : Direction.DESC, campoOrderBy))
				.toList();
	}
}
