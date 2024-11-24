package br.com.renanmatos.buypro.excecoes;

import br.com.renanmatos.buypro.dto.ErrosRequisicao;

public class RequestInvalidoException extends Exception{
	
	private ErrosRequisicao errosRequisicao;
	
	public RequestInvalidoException (String mensagemErro, ErrosRequisicao errosRequisicao, final Throwable throwable) {
		super(mensagemErro, throwable);
		this.errosRequisicao = errosRequisicao;
    	}

	//Getters e setters
	public ErrosRequisicao getErrosRequisicao() {
		return errosRequisicao;
	}

	public void setErrosRequisicao(ErrosRequisicao errosRequisicao) {
		this.errosRequisicao = errosRequisicao;
	}
	

}