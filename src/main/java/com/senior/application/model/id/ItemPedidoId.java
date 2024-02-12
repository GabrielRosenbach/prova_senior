package com.senior.application.model.id;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Classe PK do item do pedido
 */
@Embeddable
public class ItemPedidoId implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Chave id composta, representando o id do pedido
	 */
	@Column(name = "ID_PEDIDO", insertable = false, updatable = false)
	private UUID idPedido;
	
	/**
	 * Chave id composta, representando o id do produto/serviço
	 */
	@Column(name = "ID_PRODUTO_SERVICO")
	private UUID idProdutoServico;

	public ItemPedidoId() {
	}

	public ItemPedidoId(UUID idPedido, UUID idProdutoServico) {
		this.idPedido = idPedido;
		this.idProdutoServico = idProdutoServico;
	}

	public UUID getIdPedido() {
		return idPedido;
	}

	public void setIdPedido(UUID idPedido) {
		this.idPedido = idPedido;
	}

	public UUID getIdProdutoServico() {
		return idProdutoServico;
	}

	public void setIdProdutoServico(UUID idProdutoServico) {
		this.idProdutoServico = idProdutoServico;
	}
}
