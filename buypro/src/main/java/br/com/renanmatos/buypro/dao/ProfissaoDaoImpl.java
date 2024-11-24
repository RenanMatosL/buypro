package br.com.renanmatos.buypro.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.renanmatos.buypro.excecoes.RegistroJaExisteException;
import br.com.renanmatos.buypro.excecoes.RegistroNaoEncontradoException;
import br.com.renanmatos.buypro.model.Profissao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Repository
public class ProfissaoDaoImpl implements ProfissaoDao{

	//Indica a injeção do EntityManager JPA pelo Spring
	@PersistenceContext
	EntityManager entityManager;

	//Indica que o método é implementação de uma interface
	@Override
	//Método que retorna a um List de TODAS Profissões
	public List<Profissao> carregarTodasProfissoes() {
		StringBuilder jpql = new StringBuilder("");
		jpql.append("SELECT DISTINCT(p) ");
		jpql.append("FROM Profissao p ");
		jpql.append("ORDER BY p.nome ");

		//Criando a query, efetuando a busca e inserir os registros em uma lista
		TypedQuery<Profissao> typedQuery = entityManager.createQuery(jpql.toString(), Profissao.class);

		List<Profissao> listaProfissoes = typedQuery.getResultList();

		return listaProfissoes;
	}

	//Indica que o método é implementação de uma interface
	@Override
	//Método que retorna um Profissao por seu ID
	public Profissao consultarProfissaoPorId(Long idProfissao) {

		StringBuilder jpql = new StringBuilder("");
		jpql.append("SELECT DISTINCT(p) ");
		jpql.append("FROM Profissao p ");
		jpql.append("WHERE p.idProfissao = :idProfissaoP ");

		//Criando a query, efetuando a busca e inserir os registros em uma lista
		TypedQuery<Profissao> typedQuery = entityManager.createQuery(jpql.toString(), Profissao.class);

		//Parmâmetros da instrução SQL
		typedQuery.setParameter("idProfissaoP", idProfissao);

		//Tratar exceção de getSingleResult em cenário de registro não localizado
		try {
			Profissao profissao = typedQuery.getSingleResult();
			return profissao;
		} catch (NoResultException e) {
			//Registro não encontrado
			return null;
		}
	}

	//Indica que o método é implementação de uma interface
	@Override
	//Método que cadastra um novo profissao
	public Profissao salvarProfissao(Profissao profissao) throws RegistroJaExisteException{
		//Valida campos únicos (se já não existe profissao com campos únicos desse registro)
		validarCamposUnicos(profissao);

		entityManager.persist(profissao);
		return profissao;
	}

	//Indica que o método é implementação de uma interface
	@Override
	//Método que altera um profissao
	public Profissao alterarProfissao(Profissao profissao) throws RegistroNaoEncontradoException, RegistroJaExisteException {
		//Verificar se o registro existe
		if (consultarProfissaoPorId(profissao.getIdProfissao()) == null) {
			throw new RegistroNaoEncontradoException("Profissao de idProfissao " + profissao.getIdProfissao() + " não localizado");
		}

		//Valida campos únicos (se já não existe profissao com campos únicos desse registro)
		validarCamposUnicos(profissao);

		entityManager.merge(profissao);

		//Indicar ao JPA que comandos pendentes de execução (UPDATE, DELETE, por exemplo) deverão ser executados nesse momento
		entityManager.flush();

		return profissao;
	}

	//Indica que o método é implementação de uma interface
	@Override
	//Método que verifica se já não existe um profissao com os campos únicos recuperados
	public void validarCamposUnicos (Profissao profissao) throws RegistroJaExisteException{
		StringBuilder jpql = new StringBuilder("");
		jpql.append("SELECT COUNT(p) ");
		jpql.append("FROM Profissao p ");
		jpql.append("WHERE p.nome = :nomeP ");
		
		//Verificar se o cenário é de alteração
		if (profissao.getIdProfissao() != null) {
			jpql.append("	AND p.idProfissao <> :idProfissaoP ");
		}

		//Aplicando a query
		TypedQuery<Long> typedQuery = entityManager.createQuery(jpql.toString(), Long.class);

		//Parmâmetros da instrução SQL
		typedQuery.setParameter("nomeP", profissao.getNome());
		
		//Verificar se o cenário é de alteração
		if (profissao.getIdProfissao() != null) {
			typedQuery.setParameter("idProfissaoP", profissao.getIdProfissao());
		}

		//Verificar se já existe um registro com os campos únicos
		Long quantidadeRegistrosLocalizados = typedQuery.getSingleResult();

		if (quantidadeRegistrosLocalizados > 0) {
			//Registro já existe
			throw new RegistroJaExisteException("Já existe uma profissão com esses valores");
		}
	}
}
