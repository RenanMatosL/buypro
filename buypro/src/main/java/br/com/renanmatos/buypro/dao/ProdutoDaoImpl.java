package br.com.renanmatos.buypro.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.renanmatos.buypro.excecoes.RegistroNaoEncontradoException;
import br.com.renanmatos.buypro.model.Produto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Repository
public class ProdutoDaoImpl implements ProdutoDao{

	//Indica a injeção do EntityManager JPA pelo Spring
	@PersistenceContext
	EntityManager entityManager;

	@Override
	//Método que retorna a um List de TODOS Produtos
	public List<Produto> carregarTodosProdutos() {
		StringBuilder jpql = new StringBuilder("");
		jpql.append("SELECT p ");
		jpql.append("FROM Produto p ");
		jpql.append("ORDER BY p.nome ");

		//Criando a query, efetuando a busca e inserir os registros em uma lista
		TypedQuery<Produto> typedQuery = entityManager.createQuery(jpql.toString(), Produto.class);

		List<Produto> listaProdutos = typedQuery.getResultList();

		return listaProdutos;
	}

	//Indica que o método é implementação de uma interface
	@Override
	//Método que retorna um Produto por seu ID
	public Produto consultarProdutoPorId(Long idProduto) {
		return entityManager.find(Produto.class, idProduto);
	}

	//Indica que o método é implementação de uma interface
	@Override
	//Método que cadastra um novo produto
	public Produto salvarProduto(Produto produto){
		entityManager.merge(produto);
		return produto;
	}

	//Indica que o método é implementação de uma interface
	@Override
	//Método que altera um produto
	public Produto alterarProduto (Produto produto) throws RegistroNaoEncontradoException {
		//Verificar se o registro existe
		if (consultarProdutoPorId(produto.getIdProduto()) == null) {
			throw new RegistroNaoEncontradoException("Produto de idProduto " + produto.getIdProduto() + " não localizado");
		}

		entityManager.merge(produto);

		//Indicar ao JPA que comandos pendentes de execução (UPDATE, DELETE, por exemplo) deverão ser executados nesse momento
		entityManager.flush();

		return produto;
	}

	@Override
	public Produto findByCodigo(String codigo) {
		 String jpql = "SELECT p FROM Produto p WHERE p.codigo = :codigo";  
	        TypedQuery<Produto> query = entityManager.createQuery(jpql, Produto.class);  
	        query.setParameter("codigo", codigo);  
	        try {  
	            return query.getSingleResult(); // Retorna um único resultado  
	        } catch (NoResultException e) {  
	            return null; // Retorna null se não encontrar nenhum produto com esse código  
	        }  
	}
	
	@Override  
	public void excluirProduto(Produto produto) throws RegistroNaoEncontradoException {  
	    if (produto != null) {  
	        Produto produtoRemovido = entityManager.find(Produto.class, produto.getIdProduto());  
	        if (produtoRemovido != null) {  
	            entityManager.remove(produtoRemovido);  
	        } else {  
	            throw new RegistroNaoEncontradoException("Produto não encontrado para exclusão.");  
	        }  
	    } else {  
	        throw new IllegalArgumentException("Produto não pode ser nulo.");  
	    }  
	}    
}
