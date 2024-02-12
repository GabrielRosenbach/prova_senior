package com.senior.application.service;

import java.util.List;
import java.util.UUID;

import com.senior.application.model.Pedido;
import com.senior.prova.application.dto.CadastroPedidoDTO;

public interface PedidoService {

	public Pedido createPedido(CadastroPedidoDTO cadastroPedidoDTO);

	public void deletePedido(UUID id);

	public List<Pedido> listPedido(Integer inicio, Integer tamanho, Boolean ascendente, String campoOrderBy);

	public Pedido readPedido(UUID id);

	public Pedido updatePedido(UUID id, CadastroPedidoDTO cadastroPedidoDTO);

	public void atualizarTotal(UUID id);
}
