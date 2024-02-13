package com.senior.application.builder;

import java.math.BigDecimal;

import com.senior.application.enums.SituacaoProdutoServicoEnum;
import com.senior.application.enums.TipoProdutoServicoEnum;
import com.senior.prova.application.dto.CadastroProdutoServicoDTO;

public class CadastroProdutoServicoDTOBuilder {

	private CadastroProdutoServicoDTO cadastroProdutoServicoDTO;

	private CadastroProdutoServicoDTOBuilder() {
	}

	public static CadastroProdutoServicoDTOBuilder umCadastroProdutoDTO() {
		CadastroProdutoServicoDTOBuilder builder = new CadastroProdutoServicoDTOBuilder();
		builder.cadastroProdutoServicoDTO = new CadastroProdutoServicoDTO("Produto 1", BigDecimal.valueOf(20),
				TipoProdutoServicoEnum.PRODUTO.getCodigo(), SituacaoProdutoServicoEnum.ATIVADO.getCodigo());

		return builder;
	}
	
	public static CadastroProdutoServicoDTOBuilder umCadastroServicoDTO() {
		CadastroProdutoServicoDTOBuilder builder = new CadastroProdutoServicoDTOBuilder();
		builder.cadastroProdutoServicoDTO = new CadastroProdutoServicoDTO("Serviço 1", BigDecimal.valueOf(200),
				TipoProdutoServicoEnum.SERVICO.getCodigo(), SituacaoProdutoServicoEnum.ATIVADO.getCodigo());

		return builder;
	}
	
	public CadastroProdutoServicoDTOBuilder comDescricao(String descricao) {
		cadastroProdutoServicoDTO.setDescricao(descricao);
		return this;
	}

	public CadastroProdutoServicoDTOBuilder comPreco(BigDecimal preco) {
		cadastroProdutoServicoDTO.setPreco(preco);
		return this;
	}

	public CadastroProdutoServicoDTOBuilder comSituacao(Integer situacao) {
		cadastroProdutoServicoDTO.setSituacao(situacao);
		return this;
	}

	public CadastroProdutoServicoDTOBuilder comTipo(Integer tipo) {
		cadastroProdutoServicoDTO.setTipo(tipo);
		return this;
	}

	public CadastroProdutoServicoDTO agora() {
		return cadastroProdutoServicoDTO;
	}

}
