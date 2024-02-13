package com.senior.application.builder;

import static com.senior.application.builder.ProdutoServicoBuilder.umProduto;
import static com.senior.application.builder.ProdutoServicoBuilder.umServico;

import java.util.UUID;

import com.senior.application.enums.SituacaoPedidoEnum;
import com.senior.application.model.ItemPedido;
import com.senior.application.model.Pedido;

public class ItemPedidoBuilder {

	private ItemPedido itemPedido;

	private ItemPedidoBuilder() {
	}

	public static ItemPedidoBuilder umItemPedidoProduto() {
		ItemPedidoBuilder builder = new ItemPedidoBuilder();
		builder.itemPedido = new ItemPedido();
		builder.itemPedido.getItemPedidoId().setIdPedido(UUID.fromString("502754fe-cc27-467c-bf83-b61ef11ed4ef"));
		builder.itemPedido.getItemPedidoId()
				.setIdProdutoServico(UUID.fromString("d462ad5c-a29d-4d22-8b1a-e888bfe917e9"));
		builder.itemPedido.setProdutoServico(umProduto().agora());
		builder.itemPedido.setPedido(umPedido());
		builder.itemPedido.setQuantidade(5);

		return builder;
	}

	public static ItemPedidoBuilder umItemPedidoServico() {
		ItemPedidoBuilder builder = new ItemPedidoBuilder();
		builder.itemPedido = new ItemPedido();
		builder.itemPedido.getItemPedidoId().setIdPedido(UUID.fromString("502754fe-cc27-467c-bf83-b61ef11ed4ef"));
		builder.itemPedido.getItemPedidoId()
				.setIdProdutoServico(UUID.fromString("0f239f50-1c67-44bd-bfc9-1706bba14271"));
		builder.itemPedido.setProdutoServico(umServico().agora());
		builder.itemPedido.setPedido(umPedido());

		return builder;
	}

	private static Pedido umPedido() {
		Pedido pedido = new Pedido();
		pedido.setId(UUID.fromString("502754fe-cc27-467c-bf83-b61ef11ed4ef"));
		pedido.setDesconto(20F);
		pedido.setTotal(280F);
		pedido.setSituacao(SituacaoPedidoEnum.ABERTO.getCodigo());
		return pedido;

	}

	public ItemPedido agora() {
		return itemPedido;
	}

}
