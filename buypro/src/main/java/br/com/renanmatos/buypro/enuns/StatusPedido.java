package br.com.renanmatos.buypro.enuns;


import com.fasterxml.jackson.annotation.JsonValue;

//Enum presente na PRÓPRIA classe (pois é de uso exclusivo dela
public enum StatusPedido {

	PAGO(3, "Pedido Pago"),
	AGUARDANDO_PAGAMENTO(2, "Aguardando pagamento"),
	ATIVO(1, "Pedido ativo"),
	INATIVO(0, "Pedido inativo"),
	CANCELADO(-1, "Pedido Cancelado");
			
	private int value;
	private String descricao;
			
	StatusPedido (int value, String descricao) {
		this.value = value;
		this.setDescricao(descricao);
	}
			
	//Caso esse enum seja utilizado pare receber ou retornar dados JSON de requisições, essa anotação indica que deverá ser considerado o valor NUMÉRICO desse atributo
	@JsonValue
	public Integer toInt() {
		return value;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
