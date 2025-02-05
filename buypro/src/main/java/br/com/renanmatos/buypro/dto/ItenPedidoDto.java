package br.com.renanmatos.buypro.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.renanmatos.buypro.model.Pedido;
import br.com.renanmatos.buypro.validacao.ValidacaoAlteracao;
import br.com.renanmatos.buypro.validacao.ValidacaoCadastro;
import jakarta.validation.constraints.NotNull;

public class ItenPedidoDto implements Serializable {

	// Validação de preenchimento obrigatório
	@NotNull(message = "{validacao.campo-obrigatorio.produto}", groups = { ValidacaoCadastro.class,
			ValidacaoAlteracao.class })
	private ProdutoDto produto;

	@NotNull(message = "{validacao.campo-obrigatorio.quantidade}", groups = { ValidacaoCadastro.class,
			ValidacaoAlteracao.class })
	private BigDecimal quantidade;

	private BigDecimal valorUnitario;

	public ProdutoDto getProduto() {
		return produto;
	}

	public void setProduto(ProdutoDto produto) {
		this.produto = produto;
	}

	public BigDecimal getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

}
