package com.senior.application.service;

import java.util.List;
import java.util.UUID;

import com.senior.application.model.ProdutoServico;
import com.senior.prova.application.dto.CadastroProdutoServicoDTO;

public interface ProdutoServicoService {

	public ProdutoServico createProdutoServico(CadastroProdutoServicoDTO cadastroProdutoServicoDTO);

	public ProdutoServico readProdutoServico(UUID id);

	public ProdutoServico updateProdutoServico(UUID id, CadastroProdutoServicoDTO cadastroProdutoServicoDTO);

	public void deleteProdutoServico(UUID id);

	public List<ProdutoServico> list();

	public List<ProdutoServico> list(List<UUID> listId);

	public Boolean exists(UUID idProdutoServico);

}
