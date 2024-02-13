package com.senior.application.builder;

import com.senior.prova.application.dto.CadastroItemPedidoDTO;

public class CadastroItemPedidoDTOBuilder {

	private CadastroItemPedidoDTO cadastroItemPedidoDTO;

	private CadastroItemPedidoDTOBuilder() {
	}

	public static CadastroItemPedidoDTOBuilder umCadastroItemPedidoProdutoDTO() {
		CadastroItemPedidoDTOBuilder builder = new CadastroItemPedidoDTOBuilder();
		builder.cadastroItemPedidoDTO = new CadastroItemPedidoDTO();
		builder.cadastroItemPedidoDTO.setIdProdutoServico("d462ad5c-a29d-4d22-8b1a-e888bfe917e9");
		builder.cadastroItemPedidoDTO.setQuantidade(5);

		return builder;
	}
	
	public static CadastroItemPedidoDTOBuilder umCadastroItemPedidoServicoDTO() {
		CadastroItemPedidoDTOBuilder builder = new CadastroItemPedidoDTOBuilder();
		builder.cadastroItemPedidoDTO = new CadastroItemPedidoDTO();
		builder.cadastroItemPedidoDTO.setIdProdutoServico("0f239f50-1c67-44bd-bfc9-1706bba14271");

		return builder;
	}
	
	public CadastroItemPedidoDTO agora() {
		return cadastroItemPedidoDTO;
	}

}
