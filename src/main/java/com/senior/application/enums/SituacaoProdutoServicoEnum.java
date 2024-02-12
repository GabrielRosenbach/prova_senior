package com.senior.application.enums;

/**
 * Enum do Tipo do Produto ou Serviço. Optou-se por criar um Enum, para
 * diferenciar um produto de um serviço. Também facilita caso futuramente fosse
 * adicionado outros tipos
 */
public enum SituacaoProdutoServicoEnum {

	ATIVADO(1), DESATIVADO(2);

	private Integer codigo;

	private SituacaoProdutoServicoEnum(Integer codigo) {
		this.codigo = codigo;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public static SituacaoProdutoServicoEnum valueOf(Integer codigo) {

		for (SituacaoProdutoServicoEnum tipo : SituacaoProdutoServicoEnum.values()) {
			if (tipo.getCodigo() == codigo) {
				return tipo;
			}
		}

		throw new IllegalArgumentException("Invalid OrderStatus code");
	}
}