package com.senior.application.facade;

import com.senior.application.constants.MensagemServidor;
import com.senior.application.enums.SituacaoPedidoEnum;
import com.senior.application.exceptions.http.InternalErrorException;
import com.senior.application.model.Pedido;

/**
 * Facace de pedido
 */
public class PedidoFacade {
	
	/**
	 * Valida se o pedido não está Fechado
	 * @param pedido Pedido referente.
	 * @exception InternalErrorException caso o pedido informado estiver com a situação fechada
	 */
	public static void validarSituacaoFechada(Pedido pedido) {
		if(pedido.getSituacao().equals(SituacaoPedidoEnum.FECHADO.getCodigo())) {
			throw new InternalErrorException(MensagemServidor.ALTERACAO_PEDIDO_FECHADO);
		}
	}

}
