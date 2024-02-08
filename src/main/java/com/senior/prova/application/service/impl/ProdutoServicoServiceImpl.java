package com.senior.prova.application.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.senior.prova.application.dto.CadastroProdutoServicoDTO;
import com.senior.prova.application.model.ProdutoServico;
import com.senior.prova.application.repository.ProdutoServicoRepository;
import com.senior.prova.application.service.ProdutoServicoService;
import com.senior.prova.application.util.OptionalUtil;

@Service
public class ProdutoServicoServiceImpl implements ProdutoServicoService {

	@Autowired
	private ProdutoServicoRepository produtoServicoRepository;

	private final String MENSAGEM_NOT_FOUND = "Produto/Serviço não foi encontrado!";

	@Override
	@Transactional
	public ProdutoServico createProdutoServico(CadastroProdutoServicoDTO cadastroProdutoServicoDTO) {

		ProdutoServico produtoServico = new ProdutoServico(cadastroProdutoServicoDTO);

		return produtoServicoRepository.save(produtoServico);
	}

	@Override
	public ProdutoServico readProdutoServico(UUID id) {
		return OptionalUtil.tratarOptional(produtoServicoRepository.findById(id), this.MENSAGEM_NOT_FOUND);
	}

	@Override
	public ProdutoServico updateProdutoServico(UUID id, CadastroProdutoServicoDTO cadastroProdutoServicoDTO) {

		ProdutoServico produtoServico = readProdutoServico(id);

		produtoServico.setDescricao(cadastroProdutoServicoDTO.getDescricao());
		produtoServico.setTipo(cadastroProdutoServicoDTO.getTipo());

		return produtoServicoRepository.save(produtoServico);
	}

	@Override
	public void deleteProdutoServico(UUID id) {
		//Lança NotFoundException caso o id não exista
		readProdutoServico(id);
		produtoServicoRepository.deleteById(id);
	}

	@Override
	public List<ProdutoServico> list() {
		return produtoServicoRepository.findAll();
	}
}
