package br.com.renanmatos.buypro.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.renanmatos.buypro.dao.VendedorDao;
import br.com.renanmatos.buypro.model.Produto;
import br.com.renanmatos.buypro.model.Vendedor;
import br.com.renanmatos.buypro.validacao.ValidacaoAlteracao;
import br.com.renanmatos.buypro.validacao.ValidacaoCadastro;
import br.com.renanmatos.buypro.validacao.ValidacaoCadastroPedido;
import br.com.renanmatos.buypro.validacao.ValidacaoCadastroProduto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class ProdutoDto implements Serializable {

	@NotNull(message = "{validacao.campo-obrigatorio.idProduto}", groups = { ValidacaoAlteracao.class,
			ValidacaoCadastroPedido.class })
	private Long idProduto;
	
	@NotNull(message = "{validacao.campo-obrigatorio.idVendedor}", groups = { ValidacaoAlteracao.class,
			ValidacaoCadastroProduto.class })
	private Long idVendedor;


	@NotNull(message = "{validacao.campo-obrigatorio.codigo}", groups = { ValidacaoCadastro.class,
			ValidacaoAlteracao.class })
	private String codigo;

	@NotEmpty(message = "{validacao.campo-obrigatorio.nome}", groups = { ValidacaoCadastro.class,
			ValidacaoAlteracao.class })
	private String nome;

	@NotNull(message = "{validacao.campo-obrigatorio.cor}", groups = { ValidacaoCadastro.class,
			ValidacaoAlteracao.class })
	private String cor;

	@NotNull(message = "{validacao.campo-obrigatorio.descricao}", groups = { ValidacaoCadastro.class,
			ValidacaoAlteracao.class })
	private String descricao;

	@NotNull(message = "{validacao.campo-obrigatorio.categoria}", groups = { ValidacaoCadastro.class,
			ValidacaoAlteracao.class })
	private String categoria;

	@NotNull(message = "{validacao.campo-obrigatorio.preco}", groups = { ValidacaoCadastro.class,
			ValidacaoAlteracao.class })
	private BigDecimal preco;

	@NotNull(message = "{validacao.campo-obrigatorio.peso}", groups = { ValidacaoCadastro.class,
			ValidacaoAlteracao.class })
	private BigDecimal peso;

	@NotNull(message = "{validacao.campo-obrigatorio.altura}", groups = { ValidacaoCadastro.class,
			ValidacaoAlteracao.class })
	private BigDecimal altura;

	@NotNull(message = "{validacao.campo-obrigatorio.largura}", groups = { ValidacaoCadastro.class,
			ValidacaoAlteracao.class })
	private BigDecimal largura;

	@NotNull(message = "{validacao.campo-obrigatorio.comprimento}", groups = { ValidacaoCadastro.class,
			ValidacaoAlteracao.class })
	private BigDecimal comprimento;
	
	

//	public Produto converter (VendedorDao vendedorDao) {
//		Vendedor vendedor = vendedorDao.consultarVendedorPorId(idVendedor, false);
//		return new Produto (codigo, nome, descricao, cor, categoria, preco,	peso, altura, largura, comprimento, vendedor);
//	}

	public Long getIdVendedor() {
		return idVendedor;
	}
	
	public void setIdVendedor(Long idVendedor) {
		this.idVendedor = idVendedor;
	}
	
	public Long getIdProduto() {
		return idProduto;
	}

	public void setIdProduto(Long idProduto) {
		this.idProduto = idProduto;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public BigDecimal getPeso() {
		return peso;
	}

	public void setPeso(BigDecimal peso) {
		this.peso = peso;
	}

	public BigDecimal getAltura() {
		return altura;
	}

	public void setAltura(BigDecimal altura) {
		this.altura = altura;
	}

	public BigDecimal getLargura() {
		return largura;
	}

	public void setLargura(BigDecimal largura) {
		this.largura = largura;
	}

	public BigDecimal getComprimento() {
		return comprimento;
	}

	public void setComprimento(BigDecimal comprimento) {
		this.comprimento = comprimento;
	}

	
	

}
