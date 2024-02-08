package com.senior.prova.application.service;

import java.util.List;
import java.util.UUID;

import com.senior.prova.application.dto.CadastroProdutoServicoDTO;
import com.senior.prova.application.model.ProdutoServico;

public interface ProdutoServicoService {

	public ProdutoServico createProdutoServico(CadastroProdutoServicoDTO cadastroProdutoServicoDTO);

	public ProdutoServico readProdutoServico(UUID id);

	public ProdutoServico updateProdutoServico(UUID id, CadastroProdutoServicoDTO cadastroProdutoServicoDTO);

	public void deleteProdutoServico(UUID id);

	public List<ProdutoServico> list();

}
