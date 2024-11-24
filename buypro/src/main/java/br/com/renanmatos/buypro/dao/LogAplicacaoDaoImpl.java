package br.com.renanmatos.buypro.dao;

import java.util.Date;

import org.springframework.stereotype.Repository;

import br.com.renanmatos.buypro.model.LogAplicacao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class LogAplicacaoDaoImpl implements LogAplicacaoDao{

	//Indica a injeção do EntityManager JPA pelo Spring
	@PersistenceContext
	EntityManager entityManager;

	//Indica que o método é implementação de uma interface
	@Override
	//Método que cadastra um novo pedido
	public void salvarLogAplicacao(String descricao){
		LogAplicacao logAplicacao = new LogAplicacao(descricao, new Date()); 
		entityManager.persist(logAplicacao);
	}
}
