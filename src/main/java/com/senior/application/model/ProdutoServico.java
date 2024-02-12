package com.senior.application.model;

import java.util.UUID;

import com.senior.prova.application.dto.CadastroProdutoServicoDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Modelo do Produto/Serviço
 */
@Entity()
@Table(name = "PRODUTO_SERVICO")
public class ProdutoServico {

	/**
	 * Campo identificador
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	/**
	 * Descrição do produto/serviço
	 */
	private String descricao;

	/**
	 * Preço do produto/Serviço
	 */
	private Float preco;

	/**
	 * Tipo do Produto/Serviço. 1 - Produto; 2 - Serviço
	 */
	private Integer tipo;

	public ProdutoServico() {
	}

	public ProdutoServico(CadastroProdutoServicoDTO cadastroDTO) {
		this.descricao = cadastroDTO.getDescricao();
		this.preco = cadastroDTO.getPreco().floatValue();
		this.tipo = cadastroDTO.getTipo();
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Float getPreco() {
		return preco;
	}

	public void setPreco(Float preco) {
		this.preco = preco;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	/*
	 * public List<ItemPedido> getItens() { return itens; }
	 * 
	 * public void setItens(List<ItemPedido> itens) { this.itens = itens; }
	 * 
	 * @Override public int hashCode() { return Objects.hash(descricao, id, itens,
	 * preco, tipo); }
	 * 
	 * @Override public boolean equals(Object obj) { if (this == obj) return true;
	 * if (obj == null) return false; if (getClass() != obj.getClass()) return
	 * false; ProdutoServico other = (ProdutoServico) obj; return
	 * Objects.equals(descricao, other.descricao) && Objects.equals(id, other.id) &&
	 * Objects.equals(itens, other.itens) && Objects.equals(preco, other.preco) &&
	 * Objects.equals(tipo, other.tipo); }
	 * 
	 * @Override public String toString() { return "ProdutoServico [id=" + id +
	 * ", descricao=" + descricao + ", preco=" + preco + ", tipo=" + tipo +
	 * ", itens=" + itens + "]"; }
	 */
}
