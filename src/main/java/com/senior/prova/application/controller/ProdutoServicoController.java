package com.senior.prova.application.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.senior.prova.application.dto.CadastroProdutoServicoDTO;
import com.senior.prova.application.dto.ProdutoServicoDTO;
import com.senior.prova.application.mapper.ProdutoServicoMapper;
import com.senior.prova.application.service.ProdutoServicoService;


@RestController
public class ProdutoServicoController implements ProdutoServicoApi {
	
	@Autowired
	private ProdutoServicoMapper produtoServicoMapper;
	
	@Autowired
	private ProdutoServicoService produtoServicoService;

	@Override
	public ResponseEntity<ProdutoServicoDTO> createProdutoServico(CadastroProdutoServicoDTO cadastroProdutoServicoDTO) {
		 ProdutoServicoDTO produtoServicoDTO = produtoServicoMapper.toDTO(produtoServicoService.createProdutoServico(cadastroProdutoServicoDTO));
		
		return ResponseEntity.created(URI.create("/produtoServico/" + produtoServicoDTO.getId())).body(produtoServicoDTO);
	}
	
	
}
