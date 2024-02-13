package com.senior.application.builder;

import static com.senior.application.builder.ItemPedidoBuilder.umItemPedidoProduto;
import static com.senior.application.builder.ItemPedidoBuilder.umItemPedidoServico;

import java.util.Arrays;
import java.util.UUID;

import com.senior.application.enums.SituacaoPedidoEnum;
import com.senior.application.model.Pedido;

public class PedidoBuilder {

	private Pedido pedido;

	private PedidoBuilder() {
	}

	public static PedidoBuilder umPedido() {
		PedidoBuilder builder = new PedidoBuilder();
		builder.pedido = new Pedido();
		builder.pedido.setId(UUID.fromString("502754fe-cc27-467c-bf83-b61ef11ed4ef"));
		builder.pedido.setDesconto(20F);
		builder.pedido.setTotal(280F);
		builder.pedido.setSituacao(SituacaoPedidoEnum.ABERTO.getCodigo());
		builder.pedido.setItens(Arrays.asList(umItemPedidoProduto().agora(), umItemPedidoServico().agora()));

		return builder;
	}
	
	public PedidoBuilder comSituacao(Integer situacao) {
		pedido.setSituacao(situacao);

		return this;
	}

	public Pedido agora() {
		return pedido;
	}

}
