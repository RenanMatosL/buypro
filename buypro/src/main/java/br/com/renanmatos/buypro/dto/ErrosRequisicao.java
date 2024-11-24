package br.com.renanmatos.buypro.dto;

import java.util.ArrayList;
import java.util.List;

public class ErrosRequisicao {
	private List<ErroProcessamento> erros = new ArrayList();

	//Getters e setters
	public List<ErroProcessamento> getErros() {
		return erros;
	}

	public void setErros(List<ErroProcessamento> erros) {
		this.erros = erros;
	}
	

}
