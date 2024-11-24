package br.com.renanmatos.buypro.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.renanmatos.buypro.enuns.StatusPedido;
import br.com.renanmatos.buypro.excecoes.RegistroNaoEncontradoException;
import br.com.renanmatos.buypro.model.Pedido;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

@Repository
public class PedidoDaoImpl implements PedidoDao{

	//Indica a injeção do EntityManager JPA pelo Spring
	@PersistenceContext
	EntityManager entityManager;

	//Indica que o método é implementação de uma interface
	@Override
	//Método que retorna a um List de TODOS Pedidos
	public List<Pedido> carregarTodosPedidos() {
		StringBuilder jpql = new StringBuilder("");
		jpql.append("SELECT p ");
		jpql.append("FROM Pedido p ");
		jpql.append("ORDER BY p.idPedido DESC ");

		//Criando a query, efetuando a busca e inserir os registros em uma lista
		TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql.toString(), Pedido.class);

		List<Pedido> listaPedidos = typedQuery.getResultList();

		return listaPedidos;
	}

	//Indica que o método é implementação de uma interface
	@Override
	//Método que retorna um Pedido por seu ID
	public Pedido consultarPedidoPorId(Long idPedido) {

		StringBuilder jpql = new StringBuilder("");
		jpql.append("SELECT p ");
		jpql.append("FROM Pedido p ");
		jpql.append("WHERE p.idPedido = :idPedidoP ");

		//Criando a query, efetuando a busca e inserir os registros em uma lista
		TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql.toString(), Pedido.class);

		//Parmâmetros da instrução SQL
		typedQuery.setParameter("idPedidoP", idPedido);

		//Tratar exceção de getSingleResult em cenário de registro não localizado
		try {
			Pedido pedido = typedQuery.getSingleResult();
			return pedido;
		} catch (NoResultException e) {
			//Registro não encontrado
			return null;
		}
	}

	//Indica que o método é implementação de uma interface
	@Override
	//Método que cadastra um novo pedido
	public Pedido salvarPedido(Pedido pedido){
		entityManager.persist(pedido);
		return pedido;
	}

	//Indica que o método é implementação de uma interface
	@Override
	//Método que altera um pedido
	public void alterarPedido(Pedido pedido){
		entityManager.merge(pedido);

		//Indicar ao JPA que comandos pendentes de execução (UPDATE, DELETE, por exemplo) deverão ser executados nesse momento
		entityManager.flush();
	}

	//Indica que o método é implementação de uma interface
	@Override
	//Método que altera o status do cliente
	public void cancelaPedido(Long idPedido, StatusPedido statusPedidoAtivo) throws RegistroNaoEncontradoException {
		StringBuilder jpql = new StringBuilder("");
		jpql.append("UPDATE Pedido p ");
		jpql.append("SET p.statusPedido = :statusPedidoP ");
		jpql.append("WHERE p.idPedido = : idPedidoP ");

		Query query = entityManager.createQuery(jpql.toString());

		//Parmâmetros da instrução SQL
		query.setParameter("statusPedidoP", statusPedidoAtivo);
		query.setParameter("idPedidoP", idPedido);

		//Executar o UPDATE e recuperar a quantidade de linhas alteradas (caso desejar validar se alguma linha foi alterada)
		int quantidadeLinhasAfetadas = query.executeUpdate();

		//Verificar se o registro foi localizado
		if (quantidadeLinhasAfetadas == 0) {
			throw new RegistroNaoEncontradoException("Pedido de idPedido " + idPedido + " não localizado");
		}
	}
	
	
	
	
	
	//Indica que o método é implementação de uma interface
	@Override
	//Método que altera o status do pedido
	public void alterarStatusPedido(Long idPedido, StatusPedido statusPedidoAtivo) throws RegistroNaoEncontradoException{
		StringBuilder jpql = new StringBuilder("");
		jpql.append("UPDATE Pedido p ");
		jpql.append("SET p.statusPedidoAtivo = :statusPedidoAtivoP ");
		jpql.append("WHERE p.idPedido = : idPedidoP ");

		Query query = entityManager.createQuery(jpql.toString());

		//Parmâmetros da instrução SQL
		query.setParameter("statusPedidoAtivoP", statusPedidoAtivo);
		query.setParameter("idPedidoP", idPedido);

		//Executar o UPDATE e recuperar a quantidade de linhas alteradas (caso desejar validar se alguma linha foi alterada)
		int quantidadeLinhasAfetadas = query.executeUpdate();

		//Verificar se o registro foi localizado
		if (quantidadeLinhasAfetadas == 0) {
			throw new RegistroNaoEncontradoException("Pedido de idPedido " + idPedido + " não localizado");
		}
	}
	
	//Indica que o método é implementação de uma interface
	@Override
	//Método que deleta um pedido
	public void deletaPedidoFisicamente(Long idPedido) throws RegistroNaoEncontradoException {
		//buscar pedido
		Pedido pedido = entityManager.find(Pedido.class, idPedido);
		//verificar se o pedido foi encontrado
			if (pedido == null) {
				throw new RegistroNaoEncontradoException("Pedido nao encontrado com o idPedido: " + idPedido);
			}
			//remove o pedido encontrado
			entityManager.remove(pedido);
			
	}

	
}
