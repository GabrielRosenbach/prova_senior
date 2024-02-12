package com.senior.application.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.senior.application.constants.MensagemServidor;
import com.senior.application.model.ProdutoServico;
import com.senior.application.repository.ProdutoServicoRepository;
import com.senior.application.service.ProdutoServicoService;
import com.senior.application.util.OptionalUtil;
import com.senior.prova.application.dto.CadastroProdutoServicoDTO;

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

	@Override
	public ProdutoServico readProdutoServico(UUID id) {
		return OptionalUtil.tratarOptional(produtoServicoRepository.findById(id), MensagemServidor.PRODUTO_SERVICO_NAO_ENCONTRADO);
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

	@Override
	public List<ProdutoServico> list(List<UUID> listId) {
		return produtoServicoRepository.findAllById(listId);
	}

	@Override
	public Boolean exists(UUID idProdutoServico) {
		return produtoServicoRepository.existsById(idProdutoServico);
	}
}
