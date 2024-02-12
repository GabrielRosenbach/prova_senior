package com.senior.application.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.senior.application.model.ItemPedido;
import com.senior.application.model.id.ItemPedidoId;

/**
 * Repositório dos itens do pedido
 */
@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, ItemPedidoId> {
	
	public List<ItemPedido> findByItemPedidoId_IdPedido(UUID idPedido);
}
