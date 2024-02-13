package com.senior.application.builder;

import static com.senior.application.builder.CadastroItemPedidoDTOBuilder.umCadastroItemPedidoProdutoDTO;
import static com.senior.application.builder.CadastroItemPedidoDTOBuilder.umCadastroItemPedidoServicoDTO;

import java.math.BigDecimal;
import java.util.Arrays;

import com.senior.application.enums.SituacaoPedidoEnum;
import com.senior.prova.application.dto.CadastroPedidoDTO;

public class CadastroPedidoDTOBuilder {

	private CadastroPedidoDTO cadastroPedidoDTO;

	private CadastroPedidoDTOBuilder() {
	}

	public static CadastroPedidoDTOBuilder umCadastroPedidoDTO() {
		CadastroPedidoDTOBuilder builder = new CadastroPedidoDTOBuilder();
		builder.cadastroPedidoDTO = new CadastroPedidoDTO(
				Arrays.asList(umCadastroItemPedidoProdutoDTO().agora(), umCadastroItemPedidoServicoDTO().agora()));
		builder.cadastroPedidoDTO.setDesconto(BigDecimal.valueOf(20));
		builder.cadastroPedidoDTO.setSituacao(SituacaoPedidoEnum.ABERTO.getCodigo());

		return builder;
	}
	
	public CadastroPedidoDTOBuilder comDesconto(Float desconto) {
		cadastroPedidoDTO.setDesconto(BigDecimal.valueOf(desconto));
		return this;
	}

	public CadastroPedidoDTO agora() {
		return cadastroPedidoDTO;
	}

}
