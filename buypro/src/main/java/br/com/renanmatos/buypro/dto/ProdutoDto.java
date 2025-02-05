package br.com.renanmatos.buypro.dto;
import java.io.Serializable;
import java.math.BigDecimal;

import br.com.renanmatos.buypro.validacao.ValidacaoAlteracao;
import br.com.renanmatos.buypro.validacao.ValidacaoCadastro;
import br.com.renanmatos.buypro.validacao.ValidacaoCadastroPedido;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class ProdutoDto implements Serializable{

	//Validação de preenchimento obrigatório
	@NotNull(
		//Mensagem a ser exibida em caso de não conformidade no campo (recuperada de arquivo Properties)
		message="{validacao.campo-obrigatorio.idProduto}",
		//Indicamos que essa validação deverá ser aplicada somente para os grupos indicados
		groups= {ValidacaoAlteracao.class, ValidacaoCadastroPedido.class}
	)
	private Long idProduto;

	//Validação de preenchimento obrigatório (não nulo e não vazio)
	@NotEmpty(
		//Mensagem a ser exibida em caso de não conformidade no campo (recuperada de arquivo Properties)
		message="{validacao.campo-obrigatorio.nome}",
		//Indicamos que essa validação deverá ser aplicada somente para os grupos indicados
		groups= {ValidacaoCadastro.class, ValidacaoAlteracao.class}
	)
	private String nome;
	
	//Validação de preenchimento obrigatório
	@NotNull(
		//Mensagem a ser exibida em caso de não conformidade no campo (recuperada de arquivo Properties)
		message="{validacao.campo-obrigatorio.preco}",
		//Indicamos que essa validação deverá ser aplicada somente para os grupos indicados
		groups= {ValidacaoCadastro.class, ValidacaoAlteracao.class}
	)
	private BigDecimal preco;

	public Long getIdProduto() {
		return idProduto;
	}

	public void setIdProduto(Long idProduto) {
		this.idProduto = idProduto;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}
	
	

}	

