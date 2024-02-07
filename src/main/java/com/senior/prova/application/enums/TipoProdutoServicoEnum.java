package com.senior.prova.application.enums;

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