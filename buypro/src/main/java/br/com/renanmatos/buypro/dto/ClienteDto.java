package br.com.renanmatos.buypro.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.renanmatos.buypro.enuns.StatusClienteAtivo;
import br.com.renanmatos.buypro.validacao.ValidacaoAlteracao;
import br.com.renanmatos.buypro.validacao.ValidacaoCadastro;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class ClienteDto implements Serializable{

	//Validação de preenchimento obrigatório
	@NotNull(
		//Mensagem a ser exibida em caso de não conformidade no campo (recuperada de arquivo Properties)
		message="{validacao.campo-obrigatorio.idCliente}",
		//Indicamos que essa validação deverá ser aplicada somente para os grupos indicados
		groups= {ValidacaoAlteracao.class}
	)
	private Long idCliente;

	//Validação de preenchimento obrigatório (não nulo e não vazio)
	@NotEmpty(
		//Mensagem a ser exibida em caso de não conformidade no campo (recuperada de arquivo Properties)
		message="{validacao.campo-obrigatorio.nome}",
		//Indicamos que essa validação deverá ser aplicada somente para os grupos indicados
		groups= {ValidacaoCadastro.class, ValidacaoAlteracao.class}
	)
	private String nome;

	//Validação de preenchimento obrigatório (não nulo e não vazio)
	@NotEmpty(
		//Mensagem a ser exibida em caso de não conformidade no campo (recuperada de arquivo Properties)
		message="{validacao.campo-obrigatorio.cpf}",
		//Indicamos que essa validação deverá ser aplicada somente para os grupos indicados
		groups= {ValidacaoCadastro.class, ValidacaoAlteracao.class}
	)
	private String cpf;

	//Validação de preenchimento obrigatório
	@NotNull(
		//Mensagem a ser exibida em caso de não conformidade no campo (recuperada de arquivo Properties)
		message="{validacao.campo-obrigatorio.dataNascimento}",
		//Indicamos que essa validação deverá ser aplicada somente para os grupos indicados
		groups= {ValidacaoCadastro.class, ValidacaoAlteracao.class}
	)
	private Date dataNascimento;

	private Date dataCadastro;

	private StatusClienteAtivo statusClienteAtivo;

	private List<ProfissaoDto> listaProfissoes = new ArrayList();

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public StatusClienteAtivo getStatusClienteAtivo() {
		return statusClienteAtivo;
	}

	public void setStatusClienteAtivo(StatusClienteAtivo statusClienteAtivo) {
		this.statusClienteAtivo = statusClienteAtivo;
	}

	public List<ProfissaoDto> getListaProfissoes() {
		return listaProfissoes;
	}

	public void setListaProfissoes(List<ProfissaoDto> listaProfissoes) {
		this.listaProfissoes = listaProfissoes;
	}

	
}	