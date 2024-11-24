package br.com.renanmatos.buypro.excecoes;

public class RegistroJaExisteException extends Exception {
	public RegistroJaExisteException(String mensagem) {
		super(mensagem);
	}
}
