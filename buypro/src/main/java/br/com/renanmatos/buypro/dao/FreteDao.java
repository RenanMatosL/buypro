package br.com.renanmatos.buypro.dao;

import java.util.List;

import br.com.renanmatos.buypro.model.Frete;

public interface FreteDao{
	public Frete salvarFrete(Frete frete);
	public Frete consultarFretePorId(Long idFrete);
	public List<Frete> carregarTodosFretes();
}
