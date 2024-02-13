package com.senior.application.service.impl;

import static com.senior.application.builder.CadastroItemPedidoDTOBuilder.umCadastroItemPedidoProdutoDTO;
import static com.senior.application.builder.CadastroItemPedidoDTOBuilder.umCadastroItemPedidoServicoDTO;
import static com.senior.application.builder.ItemPedidoBuilder.umItemPedidoProduto;
import static com.senior.application.builder.ItemPedidoBuilder.umItemPedidoServico;
import static com.senior.application.builder.PedidoBuilder.umPedido;
import static com.senior.application.builder.ProdutoServicoBuilder.umProduto;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.senior.application.enums.SituacaoPedidoEnum;
import com.senior.application.enums.SituacaoProdutoServicoEnum;
import com.senior.application.exceptions.http.InternalErrorException;
import com.senior.application.exceptions.http.NotFoundException;
import com.senior.application.model.ItemPedido;
import com.senior.application.model.Pedido;
import com.senior.application.model.ProdutoServico;
import com.senior.application.model.id.ItemPedidoId;
import com.senior.application.repository.ItemPedidoRepository;
import com.senior.application.repository.PedidoRepository;
import com.senior.application.service.ProdutoServicoService;
import com.senior.prova.application.dto.CadastroItemPedidoDTO;

public class ItemPedidoServiceTest {

	@Spy
	@InjectMocks
	private ItemPedidoServiceImpl itemPedidoServiceImpl;

	@Mock
	private ItemPedidoRepository itemPedidoRepository;

	@Mock
	private PedidoRepository pedidoRepository;

	@Mock
	private ProdutoServicoService produtoServicoService;

	@Rule
	public ErrorCollector errorCollector = new ErrorCollector();

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void criarItemPedidoSucessoTest() {
		CadastroItemPedidoDTO cadastroDTO = umCadastroItemPedidoProdutoDTO().agora();
		Pedido pedido = umPedido().agora();

		when(pedidoRepository.findById(any(UUID.class))).thenReturn(Optional.of(pedido));
		when(produtoServicoService.readProdutoServico(any(UUID.class))).thenReturn(umProduto().agora());
		when(itemPedidoRepository.findById(any(ItemPedidoId.class))).thenReturn(Optional.empty());
		when(itemPedidoRepository.save(any(ItemPedido.class))).thenReturn(umItemPedidoProduto().agora());

		ItemPedido retorno = itemPedidoServiceImpl.createItemPedido(pedido.getId(), cadastroDTO);

		errorCollector.checkThat(retorno.getItemPedidoId().getIdPedido(), is(pedido.getId()));
		errorCollector.checkThat(retorno.getItemPedidoId().getIdProdutoServico(),
				is(UUID.fromString(cadastroDTO.getIdProdutoServico())));
		errorCollector.checkThat(retorno.getQuantidade(), is(cadastroDTO.getQuantidade()));
		verify(pedidoRepository, times(2)).findById(any(UUID.class));
		verify(produtoServicoService).readProdutoServico(any(UUID.class));
		verify(itemPedidoRepository).findById(any(ItemPedidoId.class));
		verify(itemPedidoRepository).save(any(ItemPedido.class));
	}

	@Test
	public void pedidoNaoEncontradoAoCriarItemPedidoTest() {
		exception.expect(NotFoundException.class);
		exception.expectMessage(MensagemServidor.PEDIDO_NAO_ENCONTRADO);

		CadastroItemPedidoDTO cadastroDTO = umCadastroItemPedidoProdutoDTO().agora();
		Pedido pedido = umPedido().agora();

		when(pedidoRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

		itemPedidoServiceImpl.createItemPedido(pedido.getId(), cadastroDTO);
	}

	@Test
	public void naoCrirItemPedidoQuandoSituacaoFechadaTest() {
		exception.expect(InternalErrorException.class);
		exception.expectMessage("Não é permitido alterar um pedido fechado");

		CadastroItemPedidoDTO cadastroDTO = umCadastroItemPedidoProdutoDTO().agora();
		Pedido pedido = umPedido().agora();

		when(pedidoRepository.findById(any(UUID.class)))
				.thenReturn(Optional.of(umPedido().comSituacao(SituacaoPedidoEnum.FECHADO.getCodigo()).agora()));

		itemPedidoServiceImpl.createItemPedido(pedido.getId(), cadastroDTO);
	}

	@Test
	public void impedirAdicionarItemProdutoServicoDesativadoTest() {
		exception.expect(InternalErrorException.class);
		exception.expectMessage(MensagemServidor.ADICIONAR_ITEM_PRODUTO_SERVICO_DESATIVADO);

		CadastroItemPedidoDTO cadastroDTO = umCadastroItemPedidoProdutoDTO().agora();
		Pedido pedido = umPedido().agora();

		when(pedidoRepository.findById(any(UUID.class))).thenReturn(Optional.of(umPedido().agora()));
		when(produtoServicoService.readProdutoServico(any(UUID.class)))
				.thenReturn(umProduto().comSituacao(SituacaoProdutoServicoEnum.DESATIVADO.getCodigo()).agora());

		itemPedidoServiceImpl.createItemPedido(pedido.getId(), cadastroDTO);
	}

	@Test
	public void impedirAdicionarItemProdutoSemQuantidadeTest() {
		exception.expect(InternalErrorException.class);
		exception.expectMessage(MensagemServidor.QUANTIDADE_PRODUTO_NAO_INFORMADO);

		CadastroItemPedidoDTO cadastroDTO = umCadastroItemPedidoProdutoDTO().comQuantidade(0).agora();
		Pedido pedido = umPedido().agora();

		when(pedidoRepository.findById(any(UUID.class))).thenReturn(Optional.of(umPedido().agora()));
		when(produtoServicoService.readProdutoServico(any(UUID.class))).thenReturn(umProduto().agora());

		itemPedidoServiceImpl.createItemPedido(pedido.getId(), cadastroDTO);
	}

	@Test
	public void impedirAdicionarItemServicoComQuantidadeTest() {
		exception.expect(InternalErrorException.class);
		exception.expectMessage(MensagemServidor.ADICIONAR_ITEM_PRODUTO_SERVICO_DESATIVADO);

		CadastroItemPedidoDTO cadastroDTO = umCadastroItemPedidoServicoDTO().comQuantidade(5).agora();
		Pedido pedido = umPedido().agora();

		when(pedidoRepository.findById(any(UUID.class))).thenReturn(Optional.of(umPedido().agora()));
		when(produtoServicoService.readProdutoServico(any(UUID.class))).thenReturn(umProduto().comSituacao(SituacaoPedidoEnum.FECHADO.getCodigo()).agora());

		itemPedidoServiceImpl.createItemPedido(pedido.getId(), cadastroDTO);
	}

	@Test
	public void buscarItemPedidoSucessoTest() {
		ProdutoServico produtoServico = umProduto().agora();
		Pedido pedido = umPedido().agora();

		when(itemPedidoRepository.findById(any(ItemPedidoId.class)))
				.thenReturn(Optional.ofNullable(umItemPedidoProduto().agora()));

		ItemPedido itemPedido = itemPedidoServiceImpl.readItemPedido(pedido.getId(), produtoServico.getId());
		assertNotNull(itemPedido);
		verify(itemPedidoRepository).findById(any(ItemPedidoId.class));
	}

	@Test
	public void pedidoNaoEncontradoAoBuscarItemPedidoTest() {
		exception.expect(NotFoundException.class);
		exception.expectMessage(MensagemServidor.ITEM_PEDIDO_NAO_ENCONTRADO);

		ProdutoServico produtoServico = umProduto().agora();
		Pedido pedido = umPedido().agora();

		when(itemPedidoRepository.findById(any(ItemPedidoId.class))).thenReturn(Optional.empty());

		itemPedidoServiceImpl.readItemPedido(pedido.getId(), produtoServico.getId());
	}

	@Test
	public void listaItensPedidosSucessoTest() {
		Page<ItemPedido> page = new PageImpl<>(
				Arrays.asList(umItemPedidoProduto().agora(), umItemPedidoServico().agora()));

		when(itemPedidoRepository.findAll(any(PageRequest.class))).thenReturn(page);

		List<ItemPedido> retorno = itemPedidoServiceImpl.listItensPedidos(0, 10, true, "idPedido");

		errorCollector.checkThat(retorno.size(), is(2));
		verify(itemPedidoRepository).findAll(any(PageRequest.class));
	}

	@Test
	public void listaItensPedidoSucessoTest() {
		Page<ItemPedido> page = new PageImpl<>(
				Arrays.asList(umItemPedidoProduto().agora(), umItemPedidoServico().agora()));

		when(itemPedidoRepository.findAll(any(), any(PageRequest.class))).thenReturn(page);

		List<ItemPedido> retorno = itemPedidoServiceImpl.listItensPedido(umPedido().agora().getId(), 0, 10, true,
				"idPedido");

		errorCollector.checkThat(retorno.size(), is(2));
		verify(itemPedidoRepository).findAll(any(),any(PageRequest.class));
	}

	@Test
	public void updateItemPedidoSucessoTest() {
		List<CadastroItemPedidoDTO> listCadastroDTO = Arrays
				.asList(umCadastroItemPedidoProdutoDTO().comQuantidade(10).agora());
		Pedido pedido = umPedido().agora();

		when(produtoServicoService.readProdutoServico(any(UUID.class))).thenReturn(umProduto().agora());
		when(pedidoRepository.findById(any(UUID.class))).thenReturn(Optional.of(pedido));
		when(itemPedidoRepository.findById(any(ItemPedidoId.class)))
				.thenReturn(Optional.ofNullable(umItemPedidoProduto().agora()));
		when(itemPedidoRepository.saveAll(anyList()))
				.thenReturn(Arrays.asList(umItemPedidoProduto().comQuantidade(10).agora()));

		List<ItemPedido> retorno = itemPedidoServiceImpl.updateItensPedido(pedido.getId(), listCadastroDTO);

		assertNotNull(retorno);
		verify(pedidoRepository).findById(any(UUID.class));
		verify(produtoServicoService).readProdutoServico(any(UUID.class));
		verify(itemPedidoRepository).findById(any(ItemPedidoId.class));
		verify(itemPedidoRepository).saveAll(anyList());
	}
}
