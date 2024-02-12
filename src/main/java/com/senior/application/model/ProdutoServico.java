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

	/**
	 * Situação do Produto/Serviço. 1 - Ativado; 2 - Desativado (SituacaoProdutoServicoEnum)
	 */
	private Integer situacao;

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

	public Integer getSituacao() {
		return situacao;
	}

	public void setSituacao(Integer situacao) {
		this.situacao = situacao;
	}
}
