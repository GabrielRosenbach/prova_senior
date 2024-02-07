package com.senior.prova.application.model;

import java.util.UUID;

import com.senior.prova.application.dto.CadastroProdutoServicoDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity()
@Table(name = "PRODUTO_SERVICO")
public class ProdutoServico {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	private String descricao;

	private Integer tipo;

	public ProdutoServico() {
	}

	public ProdutoServico(CadastroProdutoServicoDTO cadastroDTO) {
		this.descricao = cadastroDTO.getDescricao();
		this.tipo = cadastroDTO.getTipo();
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}
}
