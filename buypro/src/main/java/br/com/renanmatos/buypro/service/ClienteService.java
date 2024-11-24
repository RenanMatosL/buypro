package br.com.renanmatos.buypro.service;

import java.util.List;

import br.com.renanmatos.buypro.dto.ClienteDto;
import br.com.renanmatos.buypro.enuns.StatusClienteAtivo;
import br.com.renanmatos.buypro.excecoes.RegistroJaExisteException;
import br.com.renanmatos.buypro.excecoes.RegistroNaoEncontradoException;
import br.com.renanmatos.buypro.excecoes.RequestInvalidoException;
import br.com.renanmatos.buypro.model.Cliente;

public interface ClienteService{
	public List<Cliente> carregarTodosClientes(boolean recuperarPedidos);
	public Cliente consultarClientePorId(Long idCliente, boolean recuperarPedidos) ;
	public Cliente salvarCliente(Cliente cliente) throws RegistroJaExisteException;
	public Cliente alterarCliente(Cliente cliente) throws RegistroNaoEncontradoException, RegistroJaExisteException;
	public void alterarStatusCliente(Long idCliente, StatusClienteAtivo statusClienteAtivo) throws RegistroNaoEncontradoException;
	public List<ClienteDto> getListaClienteDtoPorCliente(List<Cliente> listaCliente);
	public ClienteDto getClienteDtoPorCliente(Cliente cliente);
	public Cliente getClientePorClienteDto(ClienteDto clienteDto);
	public void validarClienteDtoParaCadastro(ClienteDto clienteDto) throws RequestInvalidoException;
	public void validarClienteDtoParaAlteracao(ClienteDto clienteDto) throws RequestInvalidoException;
}