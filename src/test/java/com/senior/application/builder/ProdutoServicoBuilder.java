package com.senior.application.builder;

import java.util.UUID;

import com.senior.application.enums.SituacaoProdutoServicoEnum;
import com.senior.application.enums.TipoProdutoServicoEnum;
import com.senior.application.model.ProdutoServico;

public class ProdutoServicoBuilder {

	private ProdutoServico produtoServico;

	private ProdutoServicoBuilder() {
	}

	public static ProdutoServicoBuilder umProdutoServico() {
		ProdutoServicoBuilder builder = new ProdutoServicoBuilder();
		builder.produtoServico = new ProdutoServico();
		builder.produtoServico.setId(UUID.fromString("d462ad5c-a29d-4d22-8b1a-e888bfe917e9"));
		builder.produtoServico.setDescricao("Produto 1");
		builder.produtoServico.setPreco(20.0F);
		builder.produtoServico.setSituacao(SituacaoProdutoServicoEnum.ATIVADO.getCodigo());
		builder.produtoServico.setTipo(TipoProdutoServicoEnum.PRODUTO.getCodigo());
		
		return builder;
	}
	
	public ProdutoServicoBuilder comDescricao(String descricao) {
		produtoServico.setDescricao(descricao);
		return this;
	}

	public ProdutoServicoBuilder comPreco(Float preco) {
		produtoServico.setPreco(preco);
		return this;
	}

	public ProdutoServicoBuilder comSituacao(Integer situacao) {
		produtoServico.setSituacao(situacao);
		return this;
	}

	public ProdutoServicoBuilder comTipo(Integer tipo) {
		produtoServico.setTipo(tipo);
		return this;
	}
	
	public ProdutoServico agora() {
		return produtoServico;
	}

}
