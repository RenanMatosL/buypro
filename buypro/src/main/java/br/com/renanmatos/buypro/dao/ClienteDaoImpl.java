package br.com.renanmatos.buypro.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.renanmatos.buypro.enuns.StatusClienteAtivo;
import br.com.renanmatos.buypro.excecoes.RegistroJaExisteException;
import br.com.renanmatos.buypro.excecoes.RegistroNaoEncontradoException;
import br.com.renanmatos.buypro.model.Cliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

@Repository
public class ClienteDaoImpl implements ClienteDao {

	// Indica a injeção do EntityManager JPA pelo Spring
	@PersistenceContext
	EntityManager entityManager;

	// Indica que o método é implementação de uma interface
	@Override
	// Método que retorna a um List de TODOS Clientes
	public List<Cliente> carregarTodosClientes(
			// Indica se a relação de PEDIDOS deverá ser carregada (JOIN realizado), já que
			// por padrão tal relação é LAZY e não seria carregada
			boolean recuperarPedidos) {
		StringBuilder jpql = new StringBuilder("");
		jpql.append("SELECT DISTINCT(c) ");
		jpql.append("FROM Cliente c ");

		// Verificar se o relacionamento com Pedidos deverá ser carregado (JOIN
		// realizado). Isso pois a relação é LAZY e não será carregada automaticamente
		if (recuperarPedidos) {
			jpql.append("	LEFT JOIN FETCH c.listaPedidos ");
		}

		jpql.append("ORDER BY c.nome ");

		// Criando a query, efetuando a busca e inserir os registros em uma lista
		TypedQuery<Cliente> typedQuery = entityManager.createQuery(jpql.toString(), Cliente.class);

		List<Cliente> listaClientes = typedQuery.getResultList();

		return listaClientes;
	}

	// Indica que o método é implementação de uma interface
	@Override
	// Método que retorna um Cliente por seu ID
	public Cliente consultarClientePorId(Long idCliente,
			// Indica se a relação de PEDIDOS deverá ser carregada (JOIN realizado), já que
			// por padrão tal relação é LAZY e não seria carregada
			boolean recuperarPedidos) {

		StringBuilder jpql = new StringBuilder("");
		jpql.append("SELECT DISTINCT(c) ");
		jpql.append("FROM Cliente c ");

		// Verificar se o relacionamento com Pedidos deverá ser carregado (JOIN
		// realizado). Isso pois a relação é LAZY e não será carregada automaticamente
		if (recuperarPedidos) {
			jpql.append("	LEFT JOIN FETCH c.listaPedidos ");
		}

		jpql.append("WHERE c.idCliente = :idClienteP ");

		// Criando a query, efetuando a busca e inserir os registros em uma lista
		TypedQuery<Cliente> typedQuery = entityManager.createQuery(jpql.toString(), Cliente.class);

		// Parmâmetros da instrução SQL
		typedQuery.setParameter("idClienteP", idCliente);

		// Tratar exceção de getSingleResult em cenário de registro não localizado
		try {
			Cliente cliente = typedQuery.getSingleResult();
			return cliente;
		} catch (NoResultException e) {
			// Registro não encontrado
			return null;
		}
	}

	// Indica que o método é implementação de uma interface
	@Override
	// Método que cadastra um novo cliente
	public Cliente salvarCliente(Cliente cliente) throws RegistroJaExisteException {
		// Valida campos únicos (se já não existe cliente com campos únicos desse
		// registro)
		validarCamposUnicos(cliente);

		entityManager.persist(cliente);
		return cliente;
	}

	// Indica que o método é implementação de uma interface
	@Override
	// Método que altera um cliente
	public Cliente alterarCliente(Cliente cliente) throws RegistroNaoEncontradoException, RegistroJaExisteException {
		// Verificar se o registro existe
		if (consultarClientePorId(cliente.getIdCliente(), false) == null) {
			throw new RegistroNaoEncontradoException(
					"Cliente de idCliente " + cliente.getIdCliente() + " não localizado");
		}

		// Valida campos únicos (se já não existe cliente com campos únicos desse
		// registro)
		validarCamposUnicos(cliente);

		entityManager.merge(cliente);

		// Indicar ao JPA que comandos pendentes de execução (UPDATE, DELETE, por
		// exemplo) deverão ser executados nesse momento
		entityManager.flush();

		return cliente;
	}

	// Indica que o método é implementação de uma interface
	@Override
	// Método que altera o status do cliente
	public void alterarStatusCliente(Long idCliente, StatusClienteAtivo statusClienteAtivo)
			throws RegistroNaoEncontradoException {
		StringBuilder jpql = new StringBuilder("");
		jpql.append("UPDATE Cliente c ");
		jpql.append("SET c.statusClienteAtivo = :statusClienteAtivoP ");
		jpql.append("WHERE c.idCliente = : idClienteP ");

		Query query = entityManager.createQuery(jpql.toString());

		// Parmâmetros da instrução SQL
		query.setParameter("statusClienteAtivoP", statusClienteAtivo);
		query.setParameter("idClienteP", idCliente);

		// Executar o UPDATE e recuperar a quantidade de linhas alteradas (caso desejar
		// validar se alguma linha foi alterada)
		int quantidadeLinhasAfetadas = query.executeUpdate();

		// Verificar se o registro foi localizado
		if (quantidadeLinhasAfetadas == 0) {
			throw new RegistroNaoEncontradoException("Cliente de idCliente " + idCliente + " não localizado");
		}
	}

	// Indica que o método é implementação de uma interface
	
	// Método que verifica se já não existe um cliente com os campos únicos
	// recuperados
	//@Override
	public void validarCamposUnicos(Cliente cliente) throws RegistroJaExisteException {
		StringBuilder jpql = new StringBuilder("");
		jpql.append("SELECT COUNT(c) ");
		jpql.append("FROM Cliente c ");
		jpql.append("WHERE c.cpf = :cpfP ");

		// Verificar se o cenário é de alteração
		if (cliente.getIdCliente() != null) {
			jpql.append("	AND c.idCliente <> :idClienteP ");
		}

		// Aplicando a query
		TypedQuery<Long> typedQuery = entityManager.createQuery(jpql.toString(), Long.class);

		// Parmâmetros da instrução SQL
		typedQuery.setParameter("cpfP", cliente.getCpf());

		// Verificar se o cenário é de alteração
		if (cliente.getIdCliente() != null) {
			typedQuery.setParameter("idClienteP", cliente.getIdCliente());
		}

		// Verificar se já existe um registro com os campos únicos
		Long quantidadeRegistrosLocalizados = typedQuery.getSingleResult();

		if (quantidadeRegistrosLocalizados > 0) {
			// Registro já existe
			throw new RegistroJaExisteException("Já existe um cliente com esses valores");
		}
	}
}
