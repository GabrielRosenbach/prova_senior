package com.senior.application.enums;

import com.senior.application.constants.MensagemServidor;
import com.senior.application.exceptions.http.InternalErrorException;

/**
 * Enum do Tipo do Produto ou Serviço. Optou-se por criar um Enum, para
 * diferenciar um produto de um serviço. Também facilita caso futuramente fosse
 * adicionado outros tipos
 */
public enum TipoProdutoServicoEnum {

	PRODUTO(1), SERVICO(2);

	private Integer codigo;
	
	private TipoProdutoServicoEnum() {
	}

	private TipoProdutoServicoEnum(Integer codigo) {
		this.codigo = codigo;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public static TipoProdutoServicoEnum valueOf(Integer codigo) {

		for (TipoProdutoServicoEnum tipo : TipoProdutoServicoEnum.values()) {
			if (tipo.getCodigo() == codigo) {
				return tipo;
			}
		}

		throw new InternalErrorException(MensagemServidor.TIPO_INVALIDO_PRODUTO_SERVICO);
	}
}