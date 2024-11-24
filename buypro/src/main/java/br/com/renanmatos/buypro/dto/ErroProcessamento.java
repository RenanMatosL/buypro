package br.com.renanmatos.buypro.dto;

public class ErroProcessamento {
	
	private Integer codigoErro;
	private String descricaoErro;
	
	//Construtores
	public ErroProcessamento() {}
	
	public ErroProcessamento(Integer codigoErro, String descricaoErro) {
		this.codigoErro = codigoErro;
		this.descricaoErro = descricaoErro;
	}

	//Getters e setters
	public Integer getCodigoErro() {
		return codigoErro;
	}

	public void setCodigoErro(Integer codigoErro) {
		this.codigoErro = codigoErro;
	}

	public String getDescricaoErro() {
		return descricaoErro;
	}

	public void setDescricaoErro(String descricaoErro) {
		this.descricaoErro = descricaoErro;
	}
	
}
