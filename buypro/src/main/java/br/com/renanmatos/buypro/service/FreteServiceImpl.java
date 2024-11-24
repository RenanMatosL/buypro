package br.com.renanmatos.buypro.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.renanmatos.buypro.dao.FreteDao;
import br.com.renanmatos.buypro.model.Pedido;
import br.com.renanmatos.buypro.model.Produto;
import br.com.renanmatos.buypro.model.ProdutoPedido;

//Implementação do serviço  
@Service
public class FreteServiceImpl implements FreteService {

	// Injeção do repositório
	@Autowired
	private FreteDao freteDao;

	@Override
	public BigDecimal calculaFretePorTabela(BigDecimal peso) {
		// Lógica para calcular o frete
		if (peso.compareTo(BigDecimal.valueOf(300)) <= 0) {
			return BigDecimal.valueOf(39.90);
		} else if (peso.compareTo(BigDecimal.valueOf(500)) <= 0) {
			return BigDecimal.valueOf(40.90);
		} else if (peso.compareTo(BigDecimal.valueOf(1000)) <= 0) {
			return BigDecimal.valueOf(42.90);
		} else if (peso.compareTo(BigDecimal.valueOf(2000)) <= 0) {
			return BigDecimal.valueOf(45.90);
		} else if (peso.compareTo(BigDecimal.valueOf(3000)) <= 0) {
			return BigDecimal.valueOf(47.90);
		} else if (peso.compareTo(BigDecimal.valueOf(4000)) <= 0) {
			return BigDecimal.valueOf(49.90);
		} else if (peso.compareTo(BigDecimal.valueOf(5000)) <= 0) {
			return BigDecimal.valueOf(51.90);
		} else if (peso.compareTo(BigDecimal.valueOf(9000)) <= 0) {
			return BigDecimal.valueOf(83.90);
		} else if (peso.compareTo(BigDecimal.valueOf(13000)) <= 0) {
			return BigDecimal.valueOf(131.90);
		} else if (peso.compareTo(BigDecimal.valueOf(17000)) <= 0) {
			return BigDecimal.valueOf(146.90);
		} else if (peso.compareTo(BigDecimal.valueOf(23000)) <= 0) {
			return BigDecimal.valueOf(171.90);
		} else if (peso.compareTo(BigDecimal.valueOf(30000)) <= 0) {
			return BigDecimal.valueOf(197.90);
		} else if (peso.compareTo(BigDecimal.valueOf(40000)) <= 0) {
			return BigDecimal.valueOf(218.90);
		} else if (peso.compareTo(BigDecimal.valueOf(50000)) <= 0) {
			return BigDecimal.valueOf(233.90);
		} else if (peso.compareTo(BigDecimal.valueOf(60000)) <= 0) {
			return BigDecimal.valueOf(249.90);
		} else if (peso.compareTo(BigDecimal.valueOf(70000)) <= 0) {
			return BigDecimal.valueOf(282.90);
		} else if (peso.compareTo(BigDecimal.valueOf(80000)) <= 0) {
			return BigDecimal.valueOf(313.90);
		} else if (peso.compareTo(BigDecimal.valueOf(90000)) <= 0) {
			return BigDecimal.valueOf(349.90);
		} else if (peso.compareTo(BigDecimal.valueOf(100000)) <= 0) {
			return BigDecimal.valueOf(399.90);
		} else if (peso.compareTo(BigDecimal.valueOf(125000)) <= 0) {
			return BigDecimal.valueOf(446.90);
		} else if (peso.compareTo(BigDecimal.valueOf(150000)) <= 0) {
			return BigDecimal.valueOf(474.90);
		} else {
			return BigDecimal.valueOf(498.90);
		}
	}

	// Novo método para calcular o peso total dos produtos em um pedido
	public BigDecimal calculaPesoTotal(Pedido pedido) {
		BigDecimal pesoTotal = BigDecimal.ZERO;

		// Itera sobre todos os produtos pedidos
		for (ProdutoPedido produtoPedido : pedido.getListaProdutoPedido()) {
			Produto produto = produtoPedido.getProduto();

			// Certifique-se de que estamos lidando corretamente com o peso
			BigDecimal pesoProduto = produto.getPeso() // Peso do produto deve ser um BigDecimal
					.multiply((produtoPedido.getQuantidade())); // Converte quantidade para BigDecimal

			// Acumula o peso total
			pesoTotal = pesoTotal.add(pesoProduto);
		}

		return pesoTotal;
	}

	// Método que calcula o frete com base no pedido
	public BigDecimal calcularFrete(Pedido pedido) {
		// Calcula o peso total
		BigDecimal pesoTotal = calculaPesoTotal(pedido);

		// Calcula o frete com base no peso total
		return calculaFretePorTabela(pesoTotal);
	}

	@Override
	public BigDecimal calcularFretePorPeso(BigDecimal peso) {
		// TODO Auto-generated method stub
		return null;
	}

}
