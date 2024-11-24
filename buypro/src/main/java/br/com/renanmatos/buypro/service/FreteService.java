package br.com.renanmatos.buypro.service;

import java.math.BigDecimal;
import java.util.List;

import br.com.renanmatos.buypro.model.Pedido;
import br.com.renanmatos.buypro.model.Produto;

//Interface que define o contrato do serviço  
public interface FreteService {

	//Metodos a serem implementados	
	BigDecimal calculaFretePorTabela(BigDecimal peso);

	BigDecimal calcularFretePorPeso(BigDecimal peso);

	BigDecimal calcularFrete(Pedido pedido);

}