package br.com.renanmatos.buypro.dao;


import java.util.List;

import br.com.renanmatos.buypro.enuns.StatusClienteAtivo;
import br.com.renanmatos.buypro.excecoes.RegistroJaExisteException;
import br.com.renanmatos.buypro.excecoes.RegistroNaoEncontradoException;
import br.com.renanmatos.buypro.model.Cliente;

public interface ClienteDao{
	public List<Cliente> carregarTodosClientes(boolean recuperarPedidos);
	public Cliente consultarClientePorId(Long idCliente, boolean recuperarPedidos) ;
	public Cliente salvarCliente(Cliente cliente) throws RegistroJaExisteException;
	public Cliente alterarCliente(Cliente cliente) throws RegistroNaoEncontradoException, RegistroJaExisteException;
	public void alterarStatusCliente(Long idCliente, StatusClienteAtivo statusClienteAtivo) throws RegistroNaoEncontradoException;
	public void validarCamposUnicos  (Cliente cliente) throws RegistroJaExisteException;
}