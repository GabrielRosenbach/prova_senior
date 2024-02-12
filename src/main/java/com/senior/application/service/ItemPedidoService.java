package com.senior.application.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.senior.application.model.ItemPedido;
import com.senior.application.model.Pedido;
import com.senior.prova.application.dto.CadastroItemPedidoDTO;

public interface ItemPedidoService {

	public ItemPedido createItemPedido(UUID idPedido, CadastroItemPedidoDTO cadastroItemPedidoDTO);

	public void deleteItemPedido(UUID idPedido, UUID idProdutoServico);

	public ItemPedido readItemPedido(UUID idPedido, UUID idProdutoServico);

	public List<ItemPedido> listItensPedidos(Integer inicio, Integer tamanho, Boolean ascendente, String campoOrderBy);

	public List<ItemPedido> listItensPedido(UUID idPedido, Integer inicio, Integer tamanho, Boolean ascendente, String campoOrderBy);

	public List<ItemPedido> updateItensPedido(UUID idPedido, List<CadastroItemPedidoDTO> listCadastroItemPedidoDTO);

	public ItemPedido validarItem(Optional<UUID> optIdPedido, Optional<Pedido> optPedido, UUID optIdProdutoServico,
			Integer quantidade);
}
