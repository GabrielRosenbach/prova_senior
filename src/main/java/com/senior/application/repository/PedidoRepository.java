package com.senior.application.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.senior.application.model.Pedido;

/**
 * Repositório do pedido
 */
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, UUID> {
}
