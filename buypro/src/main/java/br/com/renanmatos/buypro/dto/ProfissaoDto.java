package br.com.renanmatos.buypro.dto;

import java.io.Serializable;
import java.util.Objects;

import br.com.renanmatos.buypro.validacao.ValidacaoAlteracao;
import br.com.renanmatos.buypro.validacao.ValidacaoCadastro;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class ProfissaoDto implements Serializable{

	//Validação de preenchimento obrigatório
	@NotNull(
		//Mensagem a ser exibida em caso de não conformidade no campo (recuperada de arquivo Properties)
		message="{validacao.campo-obrigatorio.idCliente}",
		//Indicamos que essa validação deverá ser aplicada somente para os grupos indicados
		groups= {ValidacaoAlteracao.class}
	)
	private Long idProfissao;

	//Validação de preenchimento obrigatório (não nulo e não vazio)
	@NotEmpty(
		//Mensagem a ser exibida em caso de não conformidade no campo (recuperada de arquivo Properties)
		message="{validacao.campo-obrigatorio.nome}",
		//Indicamos que essa validação deverá ser aplicada somente para os grupos indicados
		groups= {ValidacaoCadastro.class, ValidacaoAlteracao.class}
	)
	private String nome;

	public Long getIdProfissao() {
		return idProfissao;
	}

	public void setIdProfissao(Long idProfissao) {
		this.idProfissao = idProfissao;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public int hashCode() {
		return Objects.hash(idProfissao);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProfissaoDto other = (ProfissaoDto) obj;
		return Objects.equals(idProfissao, other.idProfissao);
	}

	

}