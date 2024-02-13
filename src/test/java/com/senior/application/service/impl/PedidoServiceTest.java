package com.senior.application.service.impl;

import static com.senior.application.builder.CadastroPedidoDTOBuilder.umCadastroPedidoDTO;
import static com.senior.application.builder.ItemPedidoBuilder.umItemPedidoProduto;
import static com.senior.application.builder.ItemPedidoBuilder.umItemPedidoServico;
import static com.senior.application.builder.PedidoBuilder.umPedido;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
import com.senior.application.exceptions.http.InternalErrorException;
import com.senior.application.exceptions.http.NotFoundException;
import com.senior.application.model.Pedido;
import com.senior.application.repository.PedidoRepository;
import com.senior.application.service.ItemPedidoService;
import com.senior.prova.application.dto.CadastroPedidoDTO;

public class PedidoServiceTest {

	@Spy
	@InjectMocks
	private PedidoServiceImpl pedidoServiceImpl;

	@Mock
	private ItemPedidoService itemPedidoService;

	@Mock
	private PedidoRepository pedidoRepository;

	@Rule
	public ErrorCollector errorCollector = new ErrorCollector();

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void criarPedidoSucessoTest() {
		CadastroPedidoDTO cadastroDTO = umCadastroPedidoDTO().agora();

		Pedido pedido = umPedido().agora();

		when(itemPedidoService.validarItem(any(), any(),
				eq(UUID.fromString(cadastroDTO.getItens().get(0).getIdProdutoServico())), any()))
				.thenReturn(umItemPedidoProduto().agora());

		when(itemPedidoService.validarItem(any(), any(),
				eq(UUID.fromString(cadastroDTO.getItens().get(1).getIdProdutoServico())), any()))
				.thenReturn(umItemPedidoServico().agora());

		when(pedidoRepository.save(any())).thenReturn(pedido);

		Pedido retorno = pedidoServiceImpl.createPedido(cadastroDTO);

		errorCollector.checkThat(retorno.getId(), is(pedido.getId()));
		verify(itemPedidoService, times(2)).validarItem(any(), any(), any(), any());
		verify(pedidoRepository).save(any(Pedido.class));
	}

	@Test
	public void buscarPedidoSucessoTest() {

		when(pedidoRepository.findById(any(UUID.class))).thenReturn(Optional.of(umPedido().agora()));

		Pedido retorno = pedidoServiceImpl.readPedido(umPedido().agora().getId());

		assertNotNull(retorno);
		verify(pedidoRepository).findById(any(UUID.class));
	}

	@Test
	public void pedidoNaoEncontradoTest() {
		exception.expect(NotFoundException.class);
		exception.expectMessage(MensagemServidor.PEDIDO_NAO_ENCONTRADO);

		when(pedidoRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

		pedidoServiceImpl.readPedido(umPedido().agora().getId());
	}

	@Test
	public void excluirPedidoSucessoTest() {

		when(pedidoRepository.findById(any(UUID.class))).thenReturn(Optional.of(umPedido().agora()));

		pedidoServiceImpl.deletePedido(umPedido().agora().getId());

		verify(pedidoRepository).findById(any(UUID.class));
		verify(pedidoRepository).deleteById(any(UUID.class));
	}

	@Test
	public void naoRemoverPedidoQuandoSituacaoFechadaTest() {
		exception.expect(InternalErrorException.class);
		exception.expectMessage("Não é permitido alterar um pedido fechado");

		when(pedidoRepository.findById(any(UUID.class)))
				.thenReturn(Optional.of(umPedido().comSituacao(SituacaoPedidoEnum.FECHADO.getCodigo()).agora()));

		pedidoServiceImpl.deletePedido(umPedido().agora().getId());
	}

	@Test
	public void listarPedidoSucessoTest() {
		Page<Pedido> page = new PageImpl<>(Arrays.asList(umPedido().agora()));

		when(pedidoRepository.findAll(any(PageRequest.class))).thenReturn(page);

		List<Pedido> retorno = pedidoServiceImpl.listPedido(0, 10, true, "id");

		errorCollector.checkThat(retorno.size(), is(1));
		verify(pedidoRepository).findAll(any(PageRequest.class));
	}

	@Test
	public void atualizarPedidoSucessoTest() {
		CadastroPedidoDTO cadastroDTO = umCadastroPedidoDTO().comDesconto(10F).agora();

		Pedido pedido = umPedido().agora();

		when(pedidoRepository.findById(any(UUID.class))).thenReturn(Optional.of(pedido));

		when(itemPedidoService.validarItem(any(), any(),
				eq(UUID.fromString(cadastroDTO.getItens().get(0).getIdProdutoServico())), any()))
				.thenReturn(umItemPedidoProduto().agora());

		when(itemPedidoService.validarItem(any(), any(),
				eq(UUID.fromString(cadastroDTO.getItens().get(1).getIdProdutoServico())), any()))
				.thenReturn(umItemPedidoServico().agora());

		when(pedidoRepository.save(any())).thenReturn(pedido);

		Pedido retorno = pedidoServiceImpl.updatePedido(pedido.getId(), cadastroDTO);

		errorCollector.checkThat(retorno.getId(), is(pedido.getId()));
		errorCollector.checkThat(retorno.getDesconto(), is(pedido.getDesconto()));
		verify(pedidoRepository).findById(any(UUID.class));
		verify(itemPedidoService, times(2)).validarItem(any(), any(), any(), any());
		verify(pedidoRepository).save(any(Pedido.class));
	}

	@Test
	public void naoAtualizarPedidoQuandoSituacaoFechadaTest() {
		exception.expect(InternalErrorException.class);
		exception.expectMessage("Não é permitido alterar um pedido fechado");

		CadastroPedidoDTO cadastroDTO = umCadastroPedidoDTO().comDesconto(10F).agora();

		when(pedidoRepository.findById(any(UUID.class)))
				.thenReturn(Optional.of(umPedido().comSituacao(SituacaoPedidoEnum.FECHADO.getCodigo()).agora()));

		pedidoServiceImpl.updatePedido(umPedido().agora().getId(), cadastroDTO);
	}

	@Test
	public void atualizarTotalSucessoTest() {

		Pedido pedido = umPedido().agora();
		pedido.getItens().get(0).setQuantidade(2);
		
		Float total = pedido.getTotal();

		when(pedidoRepository.findById(any(UUID.class))).thenReturn(Optional.of(pedido));

		when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

		pedidoServiceImpl.atualizarTotal(umPedido().agora().getId());
		
		errorCollector.checkThat(pedido.getTotal(), not(is(total)));
	}

}
