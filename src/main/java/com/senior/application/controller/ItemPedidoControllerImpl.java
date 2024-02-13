package com.senior.application.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.senior.application.config.ModelMapper;
import com.senior.application.exceptions.http.NotFoundException;
import com.senior.application.model.ItemPedido;
import com.senior.application.service.ItemPedidoService;
import com.senior.application.service.PedidoService;
import com.senior.prova.application.api.ItemPedidoApi;
import com.senior.prova.application.dto.CadastroItemPedidoDTO;
import com.senior.prova.application.dto.ItemPedidoDTO;

/**
 * Classe com mapeamento de requesição dos itens do pedido
 */
@RestController
public class ItemPedidoControllerImpl implements ItemPedidoApi {

	@Autowired
	private ItemPedidoService itemPedidoService;

	@Autowired
	private PedidoService pedidoService;

	private ModelMapper modelMapper = new ModelMapper();

	/**
	 * Requisição que cria um novo item no pedido
	 * 
	 * @param strIdPedido           UUID do pedido em formato String.
	 * @param cadastroItemPedidoDTO Objeto de cadastro de um item do pedido
	 * @return Um ResponseEntity create com o item adicionado
	 */
	@Override
	public ResponseEntity<ItemPedidoDTO> createItemPedido(String strIdPedido,
			CadastroItemPedidoDTO cadastroItemPedidoDTO) {
		UUID idPedido = UUID.fromString(strIdPedido);

		ItemPedido itemPedido = itemPedidoService.createItemPedido(idPedido, cadastroItemPedidoDTO);
		pedidoService.atualizarTotal(idPedido);

		StringBuilder uriStr = new StringBuilder();
		uriStr.append("/itemPedido/");
		uriStr.append(itemPedido.getItemPedidoId().getIdPedido());
		uriStr.append("/");
		uriStr.append(itemPedido.getItemPedidoId().getIdProdutoServico());

		return ResponseEntity.created(URI.create(uriStr.toString())).body(mapToDTO(itemPedido));
	}

	/**
	 * Método interno que faz a conversão do Modelo para DTO
	 * 
	 * @param itemPedido Modelo do Item do pedido
	 * @return A conversão do modelo em ItemPedidoDTO
	 */
	private ItemPedidoDTO mapToDTO(ItemPedido itemPedido) {
		ItemPedidoDTO itemDTO = modelMapper.map(itemPedido.getItemPedidoId(), ItemPedidoDTO.class);
		itemDTO.setQuantidade(itemPedido.getQuantidade());
		return itemDTO;
	}

	/**
	 * Método interno que faz a conversão de uma lista de modelos para DTOs
	 * 
	 * @param listItensPedidos Lista de modelos de ItemPedido
	 * @return Lista convertida de ItemPedidoDTO
	 */
	private List<ItemPedidoDTO> mapToDTO(List<ItemPedido> listItensPedidos) {
		return listItensPedidos.stream().map(item -> mapToDTO(item)).collect(Collectors.toList());
	}

	/**
	 * Deleta um item do pedido
	 * 
	 * @param strIdPedido      UUID do pedido em formato String
	 * @param idProdutoServico UUID do Produto/Serviço em formato String
	 */
	@Override
	public ResponseEntity<Void> deleteItemPedido(String strIdPedido, String idProdutoServico) {
		try {
			UUID idPedido = UUID.fromString(strIdPedido);
			itemPedidoService.deleteItemPedido(idPedido, UUID.fromString(idProdutoServico));
			pedidoService.atualizarTotal(idPedido);
			return ResponseEntity.noContent().build();
		} catch (NotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 * Lista os itens de um pedido
	 * 
	 * @param idPedido     UUID do pedido em formato string
	 * @param inicio       Inicio da paginação
	 * @param tamanho      Tamanho da paginação
	 * @param ascendente   Boolean se busca é ascendente ou descendente
	 * @param campoOrderBy campo que será considerado para a ordenação
	 * @return Lista de ItemPedidoDTO
	 */
	@Override
	public ResponseEntity<List<ItemPedidoDTO>> listItensPedido(String idPedido, Integer inicio, Integer tamanho,
			Boolean ascendente, String campoOrderBy) {
		return ResponseEntity.ok().body(mapToDTO(itemPedidoService.listItensPedido(UUID.fromString(idPedido), inicio,
				tamanho, ascendente, campoOrderBy)));
	}

	/**
	 * Lista os itens de todos os pedidos
	 * 
	 * @param inicio       Inicio da paginação
	 * @param tamanho      Tamanho da paginação
	 * @param ascendente   Boolean se busca é ascendente ou descendente
	 * @param campoOrderBy campo que será considerado para a ordenação
	 * @return Lista de ItemPedidoDTO
	 */
	@Override
	public ResponseEntity<List<ItemPedidoDTO>> listItensPedidos(Integer inicio, Integer tamanho, Boolean ascendente,
			String campoOrderBy) {
		return ResponseEntity.ok()
				.body(mapToDTO(itemPedidoService.listItensPedidos(inicio, tamanho, ascendente, campoOrderBy)));
	}

	/**
	 * Busca um Item de um pedido
	 * 
	 * @param idPedido         UUID do Pedido em formato String
	 * @param idProdutoServico UUID do Produto/Serviço em formato String
	 * @return O respectivo ItemPedidoDTO
	 */
	@Override
	public ResponseEntity<ItemPedidoDTO> readItemPedido(String idPedido, String idProdutoServico) {
		try {
			return ResponseEntity.ok().body(mapToDTO(
					itemPedidoService.readItemPedido(UUID.fromString(idPedido), UUID.fromString(idProdutoServico))));
		} catch (NotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 * Atualiza uma lista de itens de um pedido
	 * 
	 * @param strIdPedido           UUID do Pedido em formato String
	 * @param cadastroItemPedidoDTO Lista de objetos de Cadastro dos itens
	 * @return Lista dos itens atualizados
	 */
	@Override
	public ResponseEntity<List<ItemPedidoDTO>> updateItensPedido(String strIdPedido,
			List<CadastroItemPedidoDTO> cadastroItemPedidoDTO) {
		try {
			UUID idPedido = UUID.fromString(strIdPedido);
			List<ItemPedido> listItemPedido = itemPedidoService.updateItensPedido(idPedido, cadastroItemPedidoDTO);
			pedidoService.atualizarTotal(idPedido);
			return ResponseEntity.ok().body(mapToDTO(listItemPedido));
		} catch (NotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
