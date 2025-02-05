package br.com.renanmatos.buypro.service;

import java.util.List;

import br.com.renanmatos.buypro.dto.PedidoDto;
import br.com.renanmatos.buypro.enuns.StatusPedido;
import br.com.renanmatos.buypro.excecoes.RegistroNaoEncontradoException;
import br.com.renanmatos.buypro.excecoes.RequestInvalidoException;
import br.com.renanmatos.buypro.model.Pedido;

public interface PedidoService{
	public List<Pedido> carregarTodosPedidos();
	public Pedido consultarPedidoPorId(Long idPedido) ;
	public Pedido salvarPedido(Pedido pedido);
	public void alterarPedido(Pedido pedido);
	public List<PedidoDto> getListaPedidoDtoPorPedido(List<Pedido> listaPedido);
	public PedidoDto getPedidoDtoPorPedido(Pedido pedido);
	public Pedido getPedidoPorPedidoDto(PedidoDto pedidoDto);
	public void validarPedidoDtoParaCadastro(PedidoDto pedidoDto) throws RequestInvalidoException;
	public void alterarStatusPedido(Long idPedido, StatusPedido statusPedido) throws RegistroNaoEncontradoException;
}

	

