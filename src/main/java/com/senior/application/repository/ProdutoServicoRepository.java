package com.senior.application.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.senior.application.model.ProdutoServico;

/**
 * Repositório do Produto/Serviço
 */
@Repository
public interface ProdutoServicoRepository
		extends JpaRepository<ProdutoServico, UUID> {

}
