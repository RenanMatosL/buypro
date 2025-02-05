package br.com.renanmatos.buypro.enuns;

import com.fasterxml.jackson.annotation.JsonValue;

//Enum presente na PRÓPRIA classe (pois é de uso exclusivo dela
public enum StatusVendedor {

	ATIVO(1, "Vendedor ativo"),
	INATIVO(0, "Vendedor inativo");
			
	private int value;
	private String descricao;
			
	StatusVendedor (int value, String descricao) {
		this.value = value;
		this.descricao = descricao;
	}
			
	//Caso esse enum seja utilizado pare receber ou retornar dados JSON de requisições, essa anotação indica que deverá ser considerado o valor NUMÉRICO desse atributo
	@JsonValue
	public Integer toInt() {
		return value;
	}
}
