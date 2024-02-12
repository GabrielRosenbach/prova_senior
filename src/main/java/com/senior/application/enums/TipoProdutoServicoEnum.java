package com.senior.application.enums;

/**
 * Enum do Tipo do Produto ou Serviço. Optou-se por criar um Enum, para
 * diferenciar um produto de um serviço. Também facilita caso futuramente fosse
 * adicionado outros tipos
 */
public enum TipoProdutoServicoEnum {

	PRODUTO(1), SERVICO(2);

	private Integer codigo;

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

		throw new IllegalArgumentException("Invalid OrderStatus code");
	}
}