package com.senior.application.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.senior.application.config.ModelMapper;
import com.senior.application.exceptions.http.NotFoundException;
import com.senior.application.model.ProdutoServico;
import com.senior.application.service.ProdutoServicoService;
import com.senior.prova.application.api.ProdutoServicoApi;
import com.senior.prova.application.dto.CadastroProdutoServicoDTO;
import com.senior.prova.application.dto.ProdutoServicoDTO;

/**
 * Classe de papeamento de requisições de Produto/Serviço
 */
@RestController
public class ProdutoServicoControllerImpl implements ProdutoServicoApi {

	@Autowired
	private ProdutoServicoService produtoServicoService;

	private ModelMapper modelMapper = new ModelMapper();

	/**
	 * Cria um Produto/Serviço
	 * @param cadastroProdutoServicoDTO Objeto de cadatro de produto/Serviço
	 * @return Respectivo ProdutoServicoDTO
	 */
	@Override
	public ResponseEntity<ProdutoServicoDTO> createProdutoServico(CadastroProdutoServicoDTO cadastroProdutoServicoDTO) {

		ProdutoServico produtoServico = produtoServicoService.createProdutoServico(cadastroProdutoServicoDTO);
		ProdutoServicoDTO produtoServicoDTO = modelMapper.map(produtoServico, ProdutoServicoDTO.class);

		return ResponseEntity.created(URI.create("/produtoServico/" + produtoServicoDTO.getId()))
				.body(produtoServicoDTO);
	}

	/**
	 * lista os produtos/Serviços
	 * @return Lista de ProdutoServicoDTO
	 */
	@Override
	public ResponseEntity<List<ProdutoServicoDTO>> listProdutoServico() {

		List<ProdutoServicoDTO> listaProdutoServicoDTO = modelMapper.map(produtoServicoService.list(),
				new TypeToken<List<ProdutoServicoDTO>>() {
				}.getType());

		return ResponseEntity.ok().body(listaProdutoServicoDTO);
	}

	/**
	 * Busca um produto/Seviço
	 * @param id UUID do Produto/Serviço em formato String
	 * @return Respectivo ProdutoServicoDTO
	 */
	@Override
	public ResponseEntity<ProdutoServicoDTO> readProdutoServico(String id) {
		try {
			ProdutoServico produtoServico = produtoServicoService.readProdutoServico(UUID.fromString(id));
			return ResponseEntity.ok().body(modelMapper.map(produtoServico, ProdutoServicoDTO.class));

		} catch (NotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 * Atualiza um produto/Serviço
	 * @param id UUID do Produto/Serviço em formato String
	 * @param cadastroProdutoServicoDTO Objeto de cadastro de Produto/Serviço
	 * @return Respectivo ProdutoServicoDTO
	 */
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

	/**
	 * Deleta um Produto/Serviço
	 * @param id UUID do Produto/Serviço em formato String
	 */
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
