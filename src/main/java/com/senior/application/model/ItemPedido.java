package com.senior.application.model;

import java.util.Objects;
import java.util.UUID;

import com.senior.application.model.id.ItemPedidoId;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Modelo do Item do Pedido
 */
@Entity()
@Table(name = "ITEM_PEDIDO")
@Data
public class ItemPedido {

	/**
	 * Chave primária
	 */
	@EmbeddedId
	private ItemPedidoId itemPedidoId = new ItemPedidoId();

	/**
	 * Quantidade de produtos. Null quando item for Serviço
	 */
	private Integer quantidade;

	/**
	 * Mapeamento da referência do pedido que o item está ligado
	 */
	@ManyToOne()
	@MapsId("idPedido")
	@JoinColumn(name = "id_pedido")
	private Pedido pedido;

	/**
	 * Mapeamento da referência do produto/Serviço que o item está ligado
	 */
	@ManyToOne
	@MapsId("idProdutoServico")
	@JoinColumn(name = "id_produto_servico")
	private ProdutoServico produtoServico;

	public ItemPedido() {
	}

	public ItemPedido(UUID idPedido, UUID idProdutoServico) {
		itemPedidoId = new ItemPedidoId(idPedido, idProdutoServico);
	}
	
	public ItemPedido(Pedido pedido, ProdutoServico produtoServico) {
		this.pedido = pedido;
		this.produtoServico = produtoServico;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public ProdutoServico getProdutoServico() {
		return produtoServico;
	}

	public void setProdutoServico(ProdutoServico produtoServico) {
		this.produtoServico = produtoServico;
	}

	public ItemPedidoId getItemPedidoId() {
		return itemPedidoId;
	}

	public void setItemPedidoId(ItemPedidoId itemPedidoId) {
		this.itemPedidoId = itemPedidoId;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	@Override
	public int hashCode() {
		return Objects.hash(itemPedidoId, pedido, produtoServico, quantidade);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemPedido other = (ItemPedido) obj;
		return Objects.equals(itemPedidoId, other.itemPedidoId) && Objects.equals(pedido, other.pedido)
				&& Objects.equals(produtoServico, other.produtoServico) && Objects.equals(quantidade, other.quantidade);
	}

	@Override
	public String toString() {
		return "ItemPedido [itemPedidoId=" + itemPedidoId + ", quantidade=" + quantidade + ", pedido=" + pedido
				+ ", produtoServico=" + produtoServico + "]";
	}
}
