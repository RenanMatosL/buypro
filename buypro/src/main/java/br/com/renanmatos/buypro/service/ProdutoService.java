package br.com.renanmatos.buypro.service;


import java.util.List;

import br.com.renanmatos.buypro.dto.ProdutoDto;
import br.com.renanmatos.buypro.excecoes.RegistroNaoEncontradoException;
import br.com.renanmatos.buypro.excecoes.RequestInvalidoException;
import br.com.renanmatos.buypro.model.Produto;

public interface ProdutoService{
	public List<Produto> carregarTodosProdutos();
	public Produto consultarProdutoPorId(Long idProduto) ;
	public Produto salvarProduto(Produto produto);
	public Produto alterarProduto(Produto produto) throws RegistroNaoEncontradoException;
	public List<ProdutoDto> getListaProdutoDtoPorProduto (List<Produto> listaProduto);
	public ProdutoDto getProdutoDtoPorProduto(Produto produto);
	public Produto getProdutoPorProdutoDto(ProdutoDto produtoDto);
	public void validarProdutoDtoParaCadastro(ProdutoDto produtoDto) throws RequestInvalidoException;
	public void validarProdutoDtoParaAlteracao(ProdutoDto produtoDto) throws RequestInvalidoException;
}
