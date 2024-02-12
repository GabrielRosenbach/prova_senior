package com.senior.application.facade;

import com.senior.application.constants.MensagemServidor;
import com.senior.application.enums.SituacaoPedidoEnum;
import com.senior.application.exceptions.http.InternalErrorException;
import com.senior.application.model.Pedido;

public class PedidoFacade {
	
	public static void validarSituacaoFechada(Pedido pedido) {
		if(pedido.getSituacao().equals(SituacaoPedidoEnum.FECHADO.getCodigo())) {
			throw new InternalErrorException(MensagemServidor.ALETARACAO_PEDIDO_FECHADO);
		}
	}

}
