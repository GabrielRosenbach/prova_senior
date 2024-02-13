package com.senior.application.service.impl;

import static com.senior.application.builder.CadastroProdutoServicoDTOBuilder.umCadastroProdutoDTO;
import static com.senior.application.builder.ProdutoServicoBuilder.umProduto;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.senior.application.constants.MensagemServidor;
import com.senior.application.enums.SituacaoProdutoServicoEnum;
import com.senior.application.enums.TipoProdutoServicoEnum;
import com.senior.application.exceptions.http.InternalErrorException;
import com.senior.application.exceptions.http.NotFoundException;
import com.senior.application.model.ProdutoServico;
import com.senior.application.repository.ItemPedidoRepository;
import com.senior.application.repository.ProdutoServicoRepository;
import com.senior.prova.application.dto.CadastroProdutoServicoDTO;

public class ProdutoServicoServiceTest {

	@Spy
	@InjectMocks
	private ProdutoServicoServiceImpl produtoServicoServiceImpl;

	@Mock
	private ProdutoServicoRepository produtoServicoRepository;

	@Mock
	private ItemPedidoRepository itemPedidoRepository;

	@Rule
	public ErrorCollector errorCollector = new ErrorCollector();

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void criarProdutoServicoSucessoTest() {
		CadastroProdutoServicoDTO cadastroDTO = umCadastroProdutoDTO().agora();

		when(produtoServicoRepository.save(any())).thenReturn(umProduto().agora());

		ProdutoServico retorno = produtoServicoServiceImpl.createProdutoServico(cadastroDTO);

		verify(produtoServicoRepository).save(any());
		errorCollector.checkThat(retorno.getId(), notNullValue());
		errorCollector.checkThat(retorno.getDescricao(), is("Produto 1"));
		errorCollector.checkThat(retorno.getPreco(), is(20F));
		errorCollector.checkThat(retorno.getSituacao(), is(SituacaoProdutoServicoEnum.ATIVADO.getCodigo()));
		errorCollector.checkThat(retorno.getTipo(), is(TipoProdutoServicoEnum.PRODUTO.getCodigo()));
	}

	@Test
	public void buscarProdutoServicoSucessoTest() {
		UUID uuid = umProduto().agora().getId();

		when(produtoServicoRepository.findById(any())).thenReturn(Optional.ofNullable(umProduto().agora()));

		ProdutoServico retorno = produtoServicoServiceImpl.readProdutoServico(uuid);

		verify(produtoServicoRepository).findById(any());
		errorCollector.checkThat(retorno.getId(), is(uuid));
	}

	@Test
	public void naoEcontrarProdutoServicoAoBuscarTest() {
		exception.expect(NotFoundException.class);
		exception.expectMessage(MensagemServidor.PRODUTO_SERVICO_NAO_ENCONTRADO);

		UUID uuid = umProduto().agora().getId();

		when(produtoServicoRepository.findById(any())).thenReturn(Optional.empty());

		produtoServicoServiceImpl.readProdutoServico(uuid);
	}

	@Test
	public void atualizarProdutoServicoSucessoTest() {
		UUID uuid = umProduto().agora().getId();
		CadastroProdutoServicoDTO cadastroDTO = umCadastroProdutoDTO().comDescricao("Produto 2")
				.comPreco(BigDecimal.valueOf(50)).comSituacao(SituacaoProdutoServicoEnum.DESATIVADO.getCodigo())
				.comTipo(TipoProdutoServicoEnum.SERVICO.getCodigo()).agora();

		ProdutoServico original = umProduto().agora();

		when(produtoServicoRepository.findById(any())).thenReturn(Optional.of(original));
		when(produtoServicoRepository.save(original)).thenReturn(umProduto().comDescricao("Produto 2")
				.comPreco(50F).comSituacao(SituacaoProdutoServicoEnum.DESATIVADO.getCodigo())
				.comTipo(TipoProdutoServicoEnum.SERVICO.getCodigo()).agora());

		ProdutoServico retorno = produtoServicoServiceImpl.updateProdutoServico(uuid, cadastroDTO);

		verify(produtoServicoRepository).findById(any());
		verify(produtoServicoRepository).save(any());
		errorCollector.checkThat(retorno.getDescricao(), is(original.getDescricao()));
		errorCollector.checkThat(retorno.getSituacao(), is(original.getSituacao()));
		errorCollector.checkThat(retorno.getTipo(), is(original.getTipo()));
	}

	@Test
	public void deletarProdutoServicoSucessoTest() {
		UUID uuid = umProduto().agora().getId();

		when(produtoServicoRepository.findById(any())).thenReturn(Optional.of(umProduto().agora()));
		when(itemPedidoRepository.count(any())).thenReturn(0L);

		produtoServicoServiceImpl.deleteProdutoServico(uuid);

		verify(produtoServicoRepository).findById(any());
		verify(itemPedidoRepository).count(any());
		verify(produtoServicoRepository).deleteById(any());
	}

	@Test
	public void naoPermitirExcluirProdutoServicoVinculadoAUmPedidoTest() {
		exception.expect(InternalErrorException.class);
		exception.expectMessage(MensagemServidor.EXCLUIR_PRODUTO_SERVICO_VINCULADO_PEDIDO);

		UUID uuid = umProduto().agora().getId();

		when(produtoServicoRepository.findById(any())).thenReturn(Optional.of(umProduto().agora()));
		when(itemPedidoRepository.count(any())).thenReturn(5L);

		produtoServicoServiceImpl.deleteProdutoServico(uuid);
	}

	@Test
	public void listarProdutoServicoSucessoTest() {
		Page<ProdutoServico> page = new PageImpl<>(Arrays.asList(umProduto().agora()));

		when(produtoServicoRepository.findAll(any(PageRequest.class))).thenReturn(page);

		List<ProdutoServico> retorno = produtoServicoServiceImpl.listProdutoServico(0, 10, true, "id");

		errorCollector.checkThat(retorno.size(), is(1));
		verify(produtoServicoRepository).findAll(any(PageRequest.class));
	}

}
