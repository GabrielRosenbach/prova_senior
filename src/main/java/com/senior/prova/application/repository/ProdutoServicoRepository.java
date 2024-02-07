package com.senior.prova.application.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.senior.prova.application.model.ProdutoServico;

@Repository
public interface ProdutoServicoRepository extends JpaRepository<ProdutoServico, UUID> {
	
}
