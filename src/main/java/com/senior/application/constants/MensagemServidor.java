package com.senior.application.constants;

/**
 * Classe de constantes com mensagens do servidor
 */
public class MensagemServidor {

	public final static String PEDIDO_NAO_ENCONTRADO = "Pedido não foi encontrado!";
	public final static String ITEM_PEDIDO_NAO_ENCONTRADO = "Item do pedido não foi encontrado!";
	public final static String PRODUTO_SERVICO_NAO_ENCONTRADO = "Produto/Serviço não foi encontrado!";
	public final static String QUANTIDADE_PRODUTO_NAO_INFORMADO = "Quantidade deve ser informada apenas para Produto";
	public final static String NAO_INFORMADO_PEDIDO_PARA_ITEM = "Não informado pedido para o item do pedido";
	public final static String ALTERACAO_PEDIDO_FECHADO = "Não é permitido alterar um pedido fechado";
	public final static String EXCLUIR_PRODUTO_SERVICO_VINCULADO_PEDIDO = "Não é permitido excluir um produto/serviço que esteja vinculado em um pedido";
	public final static String ADICIONAR_ITEM_PRODUTO_SERVICO_DESATIVADO = "Não é permitido adicionar um Produto/Serviço desativado em um pedido";
	
	public final static String TIPO_INVALIDO_PRODUTO_SERVICO = "Tipo do Produto/Serviço inválido!";
}
