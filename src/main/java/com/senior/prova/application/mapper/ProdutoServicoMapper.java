package com.senior.prova.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.senior.prova.application.dto.ProdutoServicoDTO;
import com.senior.prova.application.model.ProdutoServico;

@Mapper(componentModel = "spring")
public interface ProdutoServicoMapper {

	ProdutoServico INSTANCE = Mappers.getMapper(ProdutoServico.class);
	
	@Mapping(target = "id", source = "produtoServico.id")
    @Mapping(target = "descricao", source = "produtoServico.descricao")
    @Mapping(target = "tipo", source = "produtoServico.tipo")
    ProdutoServicoDTO toDTO(ProdutoServico produtoServico);
}
