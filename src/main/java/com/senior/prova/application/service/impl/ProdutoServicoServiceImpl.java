package com.senior.prova.application.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.senior.prova.application.dto.CadastroProdutoServicoDTO;
import com.senior.prova.application.model.ProdutoServico;
import com.senior.prova.application.repository.ProdutoServicoRepository;
import com.senior.prova.application.service.ProdutoServicoService;

@Service
public class ProdutoServicoServiceImpl implements ProdutoServicoService {
	
	@Autowired
	private ProdutoServicoRepository produtoServicoRepository;

	@Override
	@Transactional
	public ProdutoServico createProdutoServico(CadastroProdutoServicoDTO cadastroProdutoServicoDTO) {
		
		ProdutoServico produtoServico = new ProdutoServico(cadastroProdutoServicoDTO);
		
		return produtoServicoRepository.save(produtoServico);
	}

}
