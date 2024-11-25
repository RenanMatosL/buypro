package br.com.renanmatos.buypro.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.renanmatos.buypro.model.Frete;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

//Implementação do repositório  
@Repository
public class FreteDaoImpl implements FreteDao {
	//Indica a injeção do EntityManager JPA pelo Spring
	@PersistenceContext
	EntityManager entityManager;

	//Indica que o método é implementação de uma interface
	@Override
	//Método que cadastra um novo pedido
	public Frete salvarFrete(Frete frete){
		entityManager.persist(frete);
		return frete;
	}
	
	//Indica que o método é implementação de uma interface
		@Override
		//Método que retorna um Frete por seu ID
		public Frete consultarFretePorId(Long idFrete) {

			StringBuilder jpql = new StringBuilder("");
			jpql.append("SELECT f ");
			jpql.append("FROM Frete f ");
			jpql.append("WHERE f.idFrete= :idfretef ");

			//Criando a query, efetuando a busca e inserir os registros em uma lista
			TypedQuery<Frete> typedQuery = entityManager.createQuery(jpql.toString(), Frete.class);

			//Parmâmetros da instrução SQL
			typedQuery.setParameter("idFreteF", idFrete);

			//Tratar exceção de getSingleResult em cenário de registro não localizado
			try {
				Frete frete = typedQuery.getSingleResult();
				return frete;
			} catch (NoResultException e) {
				//Registro não encontrado
				return null;
			}
		}
	
		//Indica que o método é implementação de uma interface
		@Override
		//Método que retorna a um List de TODOS Fretes
		public List<Frete> carregarTodosFretes() {
			StringBuilder jpql = new StringBuilder("");
			jpql.append("SELECT f ");
			jpql.append("FROM Frete f ");
			jpql.append("ORDER BY f.idFrete DESC ");

			//Criando a query, efetuando a busca e inserir os registros em uma lista
			TypedQuery<Frete> typedQuery = entityManager.createQuery(jpql.toString(), Frete.class);

			List<Frete> listaFretes = typedQuery.getResultList();

			return listaFretes;
		}


	



	
}
