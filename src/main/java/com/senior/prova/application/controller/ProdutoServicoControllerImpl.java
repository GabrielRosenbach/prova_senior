package com.senior.prova.application.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.senior.prova.application.api.ProdutoServicoApi;
import com.senior.prova.application.config.ModelMapper;
import com.senior.prova.application.dto.CadastroProdutoServicoDTO;
import com.senior.prova.application.dto.ProdutoServicoDTO;
import com.senior.prova.application.exceptions.http.NotFoundException;
import com.senior.prova.application.model.ProdutoServico;
import com.senior.prova.application.service.ProdutoServicoService;

@RestController
public class ProdutoServicoControllerImpl implements ProdutoServicoApi {

	@Autowired
	private ProdutoServicoService produtoServicoService;

	private ModelMapper modelMapper = new ModelMapper();

	@Override
	public ResponseEntity<ProdutoServicoDTO> createProdutoServico(CadastroProdutoServicoDTO cadastroProdutoServicoDTO) {

		ProdutoServico produtoServico = produtoServicoService.createProdutoServico(cadastroProdutoServicoDTO);
		ProdutoServicoDTO produtoServicoDTO = modelMapper.map(produtoServico, ProdutoServicoDTO.class);

		return ResponseEntity.created(URI.create("/produtoServico/" + produtoServicoDTO.getId()))
				.body(produtoServicoDTO);
	}

	@Override
	public ResponseEntity<List<ProdutoServicoDTO>> listProdutoServico() {

		List<ProdutoServicoDTO> listaProdutoServicoDTO = modelMapper.map(produtoServicoService.list(),
				new TypeToken<List<ProdutoServicoDTO>>() {
				}.getType());

		return ResponseEntity.ok().body(listaProdutoServicoDTO);
	}

	@Override
	public ResponseEntity<ProdutoServicoDTO> readProdutoServico(String id) {
		try {
			ProdutoServico produtoServico = produtoServicoService.readProdutoServico(UUID.fromString(id));
			return ResponseEntity.ok().body(modelMapper.map(produtoServico, ProdutoServicoDTO.class));

		} catch (NotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@Override
	public ResponseEntity<ProdutoServicoDTO> updateProdutoServico(String id,
			CadastroProdutoServicoDTO cadastroProdutoServicoDTO) {
		try {

			ProdutoServico produtoServico = produtoServicoService.updateProdutoServico(UUID.fromString(id),
					cadastroProdutoServicoDTO);
			return ResponseEntity.ok().body(modelMapper.map(produtoServico, ProdutoServicoDTO.class));

		} catch (NotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@Override
	public ResponseEntity<Void> deleteProdutoServico(String id) {
		try {
			produtoServicoService.deleteProdutoServico(UUID.fromString(id));
			return ResponseEntity.noContent().build();
		} catch (NotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
