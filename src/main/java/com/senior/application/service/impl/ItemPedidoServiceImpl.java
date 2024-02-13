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

/**
 * Classe de serviço de Item do Pedido
 */
@Service
public class ItemPedidoServiceImpl implements ItemPedidoService {

	@Autowired
	private ItemPedidoRepository itemPedidoRepository;

	@Autowired
	private ProdutoServicoService produtoServicoService;

	@Autowired
	private PedidoRepository pedidoRepository;

	/**
	 * Cria um item do Pedido
	 * 
	 * @param idPedido              UUID do pedido
	 * @param cadastroItemPedidoDTO Objeto de cadastro do item do pedido
	 * @exception InternalErrorException caso não encontrar pedido
	 * @return ItemPedido referente.
	 */
	@Override
	public ItemPedido createItemPedido(UUID idPedido, CadastroItemPedidoDTO cadastroItemPedidoDTO) {
		Pedido pedido = OptionalUtil.tratarOptional(pedidoRepository.findById(idPedido),
				MensagemServidor.PEDIDO_NAO_ENCONTRADO);

		PedidoFacade.validarSituacaoFechada(pedido);
		return itemPedidoRepository.save(validarItem(Optional.ofNullable(idPedido), Optional.ofNullable(pedido),
				UUID.fromString(cadastroItemPedidoDTO.getIdProdutoServico()), cadastroItemPedidoDTO.getQuantidade()));
	}

	/**
	 * Exclui um item do pedido
	 * 
	 * @param idPedido         UUID do pedido
	 * @param idProdutoServico UUID do produto/serviço
	 * @exception InternalErrorException caso não encontrar pedido
	 */
	@Override
	public void deleteItemPedido(UUID idPedido, UUID idProdutoServico) {
		Pedido pedido = OptionalUtil.tratarOptional(pedidoRepository.findById(idPedido),
				MensagemServidor.PEDIDO_NAO_ENCONTRADO);

		PedidoFacade.validarSituacaoFechada(pedido);
		pedido.getItens().removeIf(item -> item.getItemPedidoId().getIdProdutoServico().equals(idProdutoServico));
		pedidoRepository.save(pedido);
	}

	/**
	 * Busca um item do pedido
	 * 
	 * @param idPedido         UUID do pedido
	 * @param idProdutoServico UUID do produto/serviço
	 * @return ItemPedido referente.
	 * @exception InternalErrorException caso item do pedido não for encontrado
	 */
	@Override
	public ItemPedido readItemPedido(UUID idPedido, UUID idProdutoServico) {
		return OptionalUtil.tratarOptional(itemPedidoRepository.findById(new ItemPedidoId(idPedido, idProdutoServico)),
				MensagemServidor.ITEM_PEDIDO_NAO_ENCONTRADO);
	}

	/**
	 * Lista todos os itens da base
	 * 
	 * @param inicio       Inicio da paginação
	 * @param tamanho      Tamanho da paginação
	 * @param ascendente   Boolean se busca é ascendente ou descendente
	 * @param campoOrderBy campo que será considerado para a ordenação
	 * @return Lista de itens dos pedidos
	 */
	@Override
	public List<ItemPedido> listItensPedidos(Integer inicio, Integer tamanho, Boolean ascendente, String campoOrderBy) {
		campoOrderBy = validarCampoOrderBy(campoOrderBy);
		return itemPedidoRepository
				.findAll(PageRequest.of(inicio, tamanho, ascendente ? Direction.ASC : Direction.DESC, campoOrderBy))
				.toList();
	}

	/**
	 * Valida campoOrderBy, para adicionar a propriedade do EmbeddedId caso não
	 * informada
	 * 
	 * @param campoOrderBy campo que será considerado para a ordenação
	 * @return CampoOrderBy ajustado
	 */
	private String validarCampoOrderBy(String campoOrderBy) {
		if ((campoOrderBy.contains("idPedido") || campoOrderBy.contains("idProdutoServico"))
				&& !campoOrderBy.contains("itemPedidoId")) {
			return "itemPedidoId." + campoOrderBy;
		}

		return campoOrderBy;
	}

	/**
	 * Lista todos os itens de um pedido
	 * 
	 * @param idPedido     UUID do pedido
	 * @param inicio       Inicio da paginação
	 * @param tamanho      Tamanho da paginação
	 * @param ascendente   Boolean se busca é ascendente ou descendente
	 * @param campoOrderBy campo que será considerado para a ordenação
	 * @return Lista de itens do pedido
	 */
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

	/**
	 * Atualiza uma lista de itens do pedido.
	 * 
	 * @param idPedido                  UUID do pedido
	 * @param listCadastroItemPedidoDTO Lista de CadastroItemPedidoDTO
	 * @return List<ItemPedido> dos itens atualizados.
	 */
	@Override
	public List<ItemPedido> updateItensPedido(UUID idPedido, List<CadastroItemPedidoDTO> listCadastroItemPedidoDTO) {
		List<ItemPedido> listItemPedido = listCadastroItemPedidoDTO.stream()
				.map(cadastroItemDTO -> validarItem(Optional.ofNullable(idPedido), Optional.empty(),
						UUID.fromString(cadastroItemDTO.getIdProdutoServico()), cadastroItemDTO.getQuantidade()))
				.collect(Collectors.toList());

		return itemPedidoRepository.saveAll(listItemPedido);
	}

	/**
	 * Faz validações para o cadastro/atualização de itens do pedido
	 * 
	 * @param optIdPedido      Um Optional do UUID do pedido. Deve ser informado
	 *                         quando o pedido já existir na base
	 * @param optPedido        Um Optional do Pedido. Deve ser informado quando o
	 *                         ItemPedido referente não existir na base.
	 * @param idProdutoServico UUID do Produto/Serviço
	 * @param quantidade       Quantidade de ProdutoServiço do tipo Produto
	 *                         (somente);
	 * @return ItemPedido referente
	 * @exception InternalErrorException quando o produto/serviço referente estiver
	 *                                   desativado
	 * @exception InternalErrorException quando for informado quantidade para um
	 *                                   serviço, ou quando não for informado
	 *                                   quantidade para um produto
	 * @exception InternalErrorException caso Optional de idPedido e Pedido
	 *                                   estiverem vazios
	 * @exception InternalErrorException caso Optional de idPedido não estiver
	 *                                   vazio, mas o pedido não for encontrado na
	 *                                   base
	 * @exception InternalErrorException caso não for encontrado item pedido na base
	 *                                   e o Optional de pedido estiver vazio
	 */
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
