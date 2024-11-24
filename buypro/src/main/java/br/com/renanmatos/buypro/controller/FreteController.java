package br.com.renanmatos.buypro.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.renanmatos.buypro.service.FreteService;

//Controlador que lida com as requisições HTTP  
@RestController
@RequestMapping("/frete")
public class FreteController {

	@Autowired
	private FreteService freteService;

	@GetMapping("/frete")
	public BigDecimal calcularFrete(@RequestParam BigDecimal peso) {
		// calcular o frete aqui
		return BigDecimal.valueOf(39.90);
	}
}