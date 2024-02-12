package com.senior.application.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.senior.prova.application.dto.CadastroPedidoDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Modelo do Pedido
 */
@Entity()
@Table(name = "PEDIDO")
public class Pedido {
	
	/**
	 * Campo identificador
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	/**
	 * Desconto do pedido
	 */
	private Float desconto;

	/**
	 * Valor total do pedido
	 */
	private Float total;
	
	/**
	 * Mapeamento de itens referentes à instância do pedido
	 */
	@OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private List<ItemPedido> itens = new ArrayList<>();
	
	public Pedido() {
	}

	public Pedido(CadastroPedidoDTO cadastroPedidoDTO, List<ItemPedido> listItemPedido) {
		desconto = cadastroPedidoDTO.getDesconto().floatValue();
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Float getDesconto() {
		return desconto;
	}

	public void setDesconto(Float desconto) {
		this.desconto = desconto;
	}

	public Float getTotal() {
		return total;
	}

	public void setTotal(Float total) {
		this.total = total;
	}

	public List<ItemPedido> getItens() {
		return itens;
	}

	public void setItens(List<ItemPedido> itens) {
		this.itens = itens;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(desconto, id, itens, total);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pedido other = (Pedido) obj;
		return Objects.equals(desconto, other.desconto) && Objects.equals(id, other.id)
				&& Objects.equals(itens, other.itens) && Objects.equals(total, other.total);
	}

	@Override
	public String toString() {
		return "Pedido [id=" + id + ", desconto=" + desconto + ", total=" + total + ", itens=" + itens + "]";
	}
}