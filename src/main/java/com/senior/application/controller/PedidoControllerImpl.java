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
import com.senior.application.model.Pedido;
import com.senior.application.service.PedidoService;
import com.senior.prova.application.api.PedidoApi;
import com.senior.prova.application.dto.CadastroPedidoDTO;
import com.senior.prova.application.dto.PedidoDTO;
import com.senior.prova.application.dto.ProdutoServicoDTO;
import com.senior.prova.application.dto.RetornoItemPedidoDTO;

/**
 * Classe de mapeamento de requisições do pedido
 */
@RestController
public class PedidoControllerImpl implements PedidoApi {

	@Autowired
	private PedidoService pedidoService;

	private ModelMapper modelMapper = new ModelMapper();

	/**
	 * Cria um novo pedido
	 * 
	 * @param cadastroPedidoDTO Objeto de cadastro do Pedido
	 * @return PedidoDTO criado.
	 */
	@Override
	public ResponseEntity<PedidoDTO> createPedido(CadastroPedidoDTO cadastroPedidoDTO) {

		PedidoDTO pedidoDTO = mapToDTO(pedidoService.createPedido(cadastroPedidoDTO));
		return ResponseEntity.created(URI.create("/pedido/" + pedidoDTO.getId())).body(pedidoDTO);
	}

	/**
	 * Exclui um pedido
	 * 
	 * @param id UUID do pedido em formato String
	 */
	@Override
	public ResponseEntity<Void> deletePedido(String id) {
		try {
			pedidoService.deletePedido(UUID.fromString(id));
			return ResponseEntity.noContent().build();
		} catch (NotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 * Lista os pedidos
	 * 
	 * @param inicio       Inicio da paginação
	 * @param tamanho      Tamanho da paginação
	 * @param ascendente   Boolean se busca é ascendente ou descendente
	 * @param campoOrderBy campo que será considerado para a ordenação
	 * @return Lista de PedidoDTO
	 */
	@Override
	public ResponseEntity<List<PedidoDTO>> listPedido(Integer inicio, Integer tamanho, Boolean ascendente,
			String campoOrderBy) {
		return ResponseEntity.ok().body(pedidoService.listPedido(inicio, tamanho, ascendente, campoOrderBy).stream()
				.map(pedido -> mapToDTO(pedido)).collect(Collectors.toList()));
	}

	/**
	 * Busca um pedido
	 * 
	 * @param id UUID do pedido em formato String
	 * @return O respectivo PedidoDTO
	 */
	@Override
	public ResponseEntity<PedidoDTO> readPedido(String id) {
		try {
			return ResponseEntity.ok().body(mapToDTO(pedidoService.readPedido(UUID.fromString(id))));
		} catch (NotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 * Faz o mapeamento de modelo para DTO
	 * 
	 * @param pedido Modelo do Pedido
	 * @return PedidoDTO convertido
	 */
	private PedidoDTO mapToDTO(Pedido pedido) {
		PedidoDTO pedidoDTO = modelMapper.map(pedido, PedidoDTO.class);

		pedidoDTO.setItens(pedido.getItens().stream().map(itemPedido -> {
			RetornoItemPedidoDTO itemPedidoDTO = new RetornoItemPedidoDTO();
			itemPedidoDTO.setProdutoServico(modelMapper.map(itemPedido.getProdutoServico(), ProdutoServicoDTO.class));
			itemPedidoDTO.setQuantidade(itemPedido.getQuantidade());
			return itemPedidoDTO;
		}).toList());

		return pedidoDTO;
	}

	/**
	 * Atualiza um pedido
	 * 
	 * @param id                UUID do pedido em formato String
	 * @param cadastroPedidoDTO Objeto de cadastro do Pedido
	 * @return Respectivo PedidoDTO
	 */
	@Override
	public ResponseEntity<PedidoDTO> updatePedido(String id, CadastroPedidoDTO cadastroPedidoDTO) {
		try {
			return ResponseEntity.ok()
					.body(mapToDTO(pedidoService.updatePedido(UUID.fromString(id), cadastroPedidoDTO)));
		} catch (NotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
