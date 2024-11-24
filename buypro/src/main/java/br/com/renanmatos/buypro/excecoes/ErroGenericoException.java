package br.com.renanmatos.buypro.excecoes;

import br.com.renanmatos.buypro.dto.ErroProcessamento;

public class ErroGenericoException extends Exception{
	
	private ErroProcessamento erroProcessamento;
	
	public ErroGenericoException (String mensagemErro, ErroProcessamento erroProcessamento, final Throwable throwable) {
		super(mensagemErro, throwable);
		this. erroProcessamento = erroProcessamento;
    	}

	//Getters e setters
	public ErroProcessamento getErroProcessamento() {
		return erroProcessamento;
	}

	public void setErroProcessamento(ErroProcessamento erroProcessamento) {
		this.erroProcessamento = erroProcessamento;
	}
	

}
