package br.com.renanmatos.buypro.dao;

import java.util.List;

import br.com.renanmatos.buypro.enuns.StatusPedido;
import br.com.renanmatos.buypro.excecoes.RegistroNaoEncontradoException;
import br.com.renanmatos.buypro.model.Pedido;

public interface PedidoDao{
	public List<Pedido> carregarTodosPedidos();
	public Pedido consultarPedidoPorId(Long idPedido) ;
	public Pedido salvarPedido(Pedido pedido);
	public void alterarPedido(Pedido pedido);
	public void alterarStatusPedido(Long idPedido, StatusPedido statusPedido) throws RegistroNaoEncontradoException;
	public void deletaPedidoFisicamente (Long idPedido) throws RegistroNaoEncontradoException;
	public void cancelaPedido(Long idPedido, StatusPedido statusPedidoAtivo) throws RegistroNaoEncontradoException;
}
