package br.com.renanmatos.buypro.service;

import java.util.List;

import br.com.renanmatos.buypro.dto.ItenPedidoDto;
import br.com.renanmatos.buypro.excecoes.RegistroNaoEncontradoException;
import br.com.renanmatos.buypro.excecoes.RequestInvalidoException;

public interface ProdutoPedidoService{
	public List<ItenPedidoDto> carregarTodosProdutosPedidos();
	public ItenPedidoDto consultarProdutoPedidoPorId(Long idProdutoPedido) ;
	public ItenPedidoDto salvarProdutoPedido(ItenPedidoDto produtoPedido);
	public ItenPedidoDto alterarProdutoPedido(ItenPedidoDto produtoPedido) throws RegistroNaoEncontradoException;
	public List<ItenPedidoDto> getListaProdutoPedidoDtoPorProdutoPedido (List<ItenPedidoDto> listaProdutoPedido);
	public ItenPedidoDto getProdutoPedidoDtoPorProdutoPedido(ItenPedidoDto produtoPedido);
	public ItenPedidoDto getProdutoPedidoPorProdutoPedidoDto(ItenPedidoDto produtoPedidoDto);
	public void validarProdutoPedidoDtoParaCadastro(ItenPedidoDto produtoPedidoDto) throws RequestInvalidoException;
	public void validarProdutoPedidoDtoParaAlteracao(ItenPedidoDto produtoPedidoDto) throws RequestInvalidoException;
}
