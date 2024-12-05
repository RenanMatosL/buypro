package br.com.renanmatos.buypro.dao;


import java.util.List;

import br.com.renanmatos.buypro.dto.ProdutoDto;
import br.com.renanmatos.buypro.excecoes.RegistroNaoEncontradoException;
import br.com.renanmatos.buypro.model.Produto;

public interface ProdutoDao{
	public List<Produto> carregarTodosProdutos();
	public Produto consultarProdutoPorId(Long idProduto) ;
	public Produto salvarProduto(Produto produto);
	public Produto alterarProduto(Produto produto) throws RegistroNaoEncontradoException;
	public Produto findByCodigo(String codigo);
	 void excluirProduto(Produto produto) throws RegistroNaoEncontradoException;

	 
}  
