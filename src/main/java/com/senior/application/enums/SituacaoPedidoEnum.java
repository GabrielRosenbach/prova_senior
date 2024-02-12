package com.senior.application.enums;

/**
 * Enum do Tipo do Produto ou Serviço. Optou-se por criar um Enum, para
 * diferenciar um produto de um serviço. Também facilita caso futuramente fosse
 * adicionado outros tipos
 */
public enum SituacaoPedidoEnum {

	ABERTO(1), FECHADO(2);

	private Integer codigo;

	private SituacaoPedidoEnum(Integer codigo) {
		this.codigo = codigo;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public static SituacaoPedidoEnum valueOf(Integer codigo) {

		for (SituacaoPedidoEnum tipo : SituacaoPedidoEnum.values()) {
			if (tipo.getCodigo() == codigo) {
				return tipo;
			}
		}

		throw new IllegalArgumentException("Invalid OrderStatus code");
	}
}