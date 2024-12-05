package br.com.renanmatos.buypro.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.renanmatos.buypro.enuns.StatusPedido;
import br.com.renanmatos.buypro.model.Frete;
import br.com.renanmatos.buypro.model.ProdutoPedido;
import br.com.renanmatos.buypro.model.Vendedor;
import br.com.renanmatos.buypro.validacao.NotEmptyList;
import br.com.renanmatos.buypro.validacao.ValidacaoAlteracao;
import br.com.renanmatos.buypro.validacao.ValidacaoCadastroPedido;
import jakarta.validation.constraints.NotNull;

public class PedidoDto implements Serializable{

	//Validação de preenchimento obrigatório
	@NotNull(
		//Mensagem a ser exibida em caso de não conformidade no campo (recuperada de arquivo Properties)
		message="{validacao.campo-obrigatorio.idPedido}",
		//Indicamos que essa validação deverá ser aplicada somente para os grupos indicados
		groups= {ValidacaoAlteracao.class}
	)
	private Long idPedido;

	private Date protocoloDataPedido;
	private LocalDate dataPedido;
	private StatusPedido statusPedido;
	private BigDecimal valorPedido;
	private BigDecimal valorFrete;
	private BigDecimal valorPedidoComFrete;

	//Validação de preenchimento obrigatório
	@NotNull(
		//Mensagem a ser exibida em caso de não conformidade no campo (recuperada de arquivo Properties)
		message="{validacao.campo-obrigatorio.idCliente}",
		//Indicamos que essa validação deverá ser aplicada somente para os grupos indicados
		groups= {ValidacaoCadastroPedido.class}
	)
	private Long idCliente;

	//Anotação customizada para validação de lista vazia
	@NotEmptyList(
		//Mensagem a ser exibida em caso de não conformidade no campo (recuperada de arquivo Properties)
		message="{validacao.campo-obrigatorio.listaItenPedido}",
		//Indicamos que essa validação deverá ser aplicada somente para os grupos indicados
		groups= {ValidacaoCadastroPedido.class}
	)	
	private List <ItenPedidoDto> listaItenPedido = new ArrayList <> ();
	
	//Getters e setters


	public Long getIdPedido() {
		return idPedido;
	}

	public void setIdPedido(Long idPedido) {
		this.idPedido = idPedido;
	}

	public Date getProtocoloDataPedido() {
		return protocoloDataPedido;
	}

	public void setProtocoloDataPedido(Date protocoloDataPedido) {
		this.protocoloDataPedido = protocoloDataPedido;
	}

	public LocalDate getDataPedido() {
		return dataPedido;
	}

	public void setDataPedido(LocalDate dataPedido) {
		this.dataPedido = dataPedido;
	}

	public StatusPedido getStatusPedido() {
		return statusPedido;
	}

	public void setStatusPedido(StatusPedido statusPedido) {
		this.statusPedido = statusPedido;
	}

	public BigDecimal getValorPedido() {
		return valorPedido;
	}

	public void setValorPedido(BigDecimal valorPedido) {
		this.valorPedido = valorPedido;
	}

	public BigDecimal getValorFrete() {
		return valorFrete;
	}

	public void setValorFrete(BigDecimal valorFrete) {
		this.valorFrete = valorFrete;
	}

	public BigDecimal getValorPedidoComFrete() {
		return valorPedidoComFrete;
	}

	public void setValorPedidoComFrete(BigDecimal valorPedidoComFrete) {
		this.valorPedidoComFrete = valorPedidoComFrete;
	}


	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public List<ItenPedidoDto> getListaItenPedido() {
		return listaItenPedido;
	}

	public void setListaItenPedido(List<ProdutoPedido> list) {
		this.listaItenPedido = listaItenPedido;
	}


	

		
}

