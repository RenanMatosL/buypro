package br.com.renanmatos.buypro.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import br.com.renanmatos.buypro.dao.ProdutoDao;
import br.com.renanmatos.buypro.dto.ErroProcessamento;
import br.com.renanmatos.buypro.dto.ErrosRequisicao;
import br.com.renanmatos.buypro.dto.ProdutoDto;
import br.com.renanmatos.buypro.excecoes.RegistroNaoEncontradoException;
import br.com.renanmatos.buypro.excecoes.RequestInvalidoException;
import br.com.renanmatos.buypro.model.Produto;
import br.com.renanmatos.buypro.validacao.ValidacaoAlteracao;
import br.com.renanmatos.buypro.validacao.ValidacaoCadastro;
import jakarta.validation.ConstraintViolation;

@Service
public class ProdutoServiceImpl implements ProdutoService{

	//Objeto de log
	private static final Log logger = LogFactory.getLog(ProdutoServiceImpl.class);

	//Injeção de dependência (será injetada classe que IMPLEMENTA essa interface)
	@Autowired
	private ProdutoDao produtoDao;

	//Injeção de dependência de Bean Validador Bean Validation vinculado com o MessageSource de nosso arquivo properties de mensagens
	@Autowired
	//Indicamos o nome do bean que desejamos injetar
	@Qualifier("localValidatorFactoryBeanPadrao")
	private LocalValidatorFactoryBean localValidatorFactoryBean;

	//Configuração de transação
	@Transactional(
		/*Indica ao Spring que o método não precisa ser executado com uma transação, onde se JÁ houver uma transação aberta, esse método fará uso dela, mas se NÃO houver uma 
		transação aberta, o Spring NÃO deverá criar uma para executar esse método*/
		propagation=Propagation.SUPPORTS
	)
	//Indica que o método é implementação de uma interface
	@Override
	//Método que retorna a um List de TODOS Produtos
	public List<Produto> carregarTodosProdutos() {
		try{
			return produtoDao.carregarTodosProdutos();
		}catch(Exception e){
			//Logar o erro ocorrido
			logger.error("Erro ao consultar todos os produtos", e);

			//Lançar a exceção original
			throw e;
		}
	}

	//Configuração de transação
	@Transactional(
		/*Indica ao Spring que o método não precisa ser executado com uma transação, onde se JÁ houver uma transação aberta, esse método fará uso dela, mas se NÃO houver uma 
		transação aberta, o Spring NÃO deverá criar uma para executar esse método*/
		propagation=Propagation.SUPPORTS
	)
	//Indica que o método é implementação de uma interface
	@Override
	//Método que retorna um Produto por seu ID
	public Produto consultarProdutoPorId(Long idProduto) {
		try{
			return produtoDao.consultarProdutoPorId(idProduto);
		}catch(Exception e){
			//Logar o erro ocorrido
			logger.error("Erro ao consultar o produto por ID. ID_PRODUTO " + idProduto, e);

			//Lançar a exceção original
			throw e;
		}
	}

	//Configuração de transação
	@Transactional(
		/*Indica ao Spring que o método deverá ser executado com transação aberta, onde se já houver uma transação aberta, 
		deverá utilizar essa transação aberta (ao invés de criar nova), caso contrário, deverá criar uma nova transação*/
		propagation=Propagation.REQUIRED
	)
	//Indica que o método é implementação de uma interface
	@Override
	//Método que cadastra um novo produto
	public Produto salvarProduto(Produto produto){
		try{
			produtoDao.salvarProduto(produto);
			return produto;
		}catch(Exception e){
			//Logar o erro ocorrido
			logger.error("Erro ao cadastrar o produto. NOME: " + produto.getNome(), e);

			//Lançar a exceção original
			throw e;
		}
	}

	//Configuração de transação
	@Transactional(
		/*Indica ao Spring que o método deverá ser executado com transação aberta, onde se já houver uma transação aberta, 
		deverá utilizar essa transação aberta (ao invés de criar nova), caso contrário, deverá criar uma nova transação*/
		propagation=Propagation.REQUIRED
	)
	//Indica que o método é implementação de uma interface
	@Override
	//Método que altera um produto
	public Produto alterarProduto (Produto produto) throws RegistroNaoEncontradoException {
		try{
			produtoDao.alterarProduto(produto);
			return produto;
		}catch(RegistroNaoEncontradoException e){
			throw e;
		}catch(Exception e){
			//Logar o erro ocorrido
			logger.error("Erro ao alterar o produto. ID_PRODUTO " + produto.getIdProduto(), e);

			//Lançar a exceção original
			throw e;
		}
	}

	//Indica que o método é implementação de uma interface
	@Override
	//Método que recebe uma lista de Produto e retorna lista de ProdutoDto
	public List<ProdutoDto> getListaProdutoDtoPorProduto(List<Produto> listaProduto) {
		if (listaProduto == null || listaProduto.size() == 0) {
			return null;
		}else {
			List<ProdutoDto> listaProdutoDto = new ArrayList();
			
			for (Produto produto : listaProduto) {
				ProdutoDto produtoDto = getProdutoDtoPorProduto(produto);
				listaProdutoDto.add(produtoDto);
			}
			
			return listaProdutoDto;
		}
	}
	
	//Indica que o método é implementação de uma interface
	@Override
	//Método que recebe um Produto e retorna um ProdutoDto
	public ProdutoDto getProdutoDtoPorProduto(Produto produto) {
		if (produto != null) {
			ProdutoDto produtoDto = new ProdutoDto();
			produtoDto.setIdProduto(produto.getIdProduto());
			produtoDto.setNome(produto.getNome());
			produtoDto.setPreco(produto.getPreco());		
			
			return produtoDto;
		}else {
			return null;
		}
	}

	//Indica que o método é implementação de uma interface
	@Override
	//Método que recebe um ProdutoDto e retorna um Produto
	public Produto getProdutoPorProdutoDto(ProdutoDto produtoDto) {
		if (produtoDto != null) {
			Produto produto = new Produto();
			produto.setIdProduto(produtoDto.getIdProduto());
			produto.setNome(produtoDto.getNome());
			produto.setPreco(produtoDto.getPreco());
			
			return produto;
		}else {
			return null;
		}
	}

	//Indica que o método é implementação de uma interface
	@Override
	//Método que valida campos DTO para CADASTRO
	public void validarProdutoDtoParaCadastro(ProdutoDto produtoDto) throws RequestInvalidoException {
		//Validar o registro e recuperar os erros localizados
		Set<ConstraintViolation<ProdutoDto>> listaConstraintViolationErrosValidacao = 
			localValidatorFactoryBean.validate(
				//Objeto a ser validado
				produtoDto
				//Interfaces que representam o evento/grupo de validação a ser considerado (somente serão validados atributos vinculados com essas interfaces) 
				,ValidacaoCadastro.class
			);
		 
		//Verificar se foram localizados erros
		if (listaConstraintViolationErrosValidacao != null && !listaConstraintViolationErrosValidacao.isEmpty()) {
			//Foram localizados erros. Preparar objeto a ser lançado junto à exceção com a lista de erros
			ErrosRequisicao errosRequisicao = new ErrosRequisicao();
			
			//Iterar pelos erros e popular o objeto a ser retornado junto à exceção
			for (ConstraintViolation<ProdutoDto> constraintViolationErroValidacao : listaConstraintViolationErrosValidacao) {
				errosRequisicao.getErros().add(new ErroProcessamento(null, constraintViolationErroValidacao.getMessage()));
			}
			
			throw new RequestInvalidoException("Requisição inválida", errosRequisicao, null);
		}
	}
	
	//Indica que o método é implementação de uma interface
	@Override
	//Método que valida campos DTO para CADASTRO
	public void validarProdutoDtoParaAlteracao(ProdutoDto produtoDto) throws RequestInvalidoException {
		//Validar o registro e recuperar os erros localizados
		Set<ConstraintViolation<ProdutoDto>> listaConstraintViolationErrosValidacao = 
			localValidatorFactoryBean.validate(
				//Objeto a ser validado
				produtoDto
				//Interfaces que representam o evento/grupo de validação a ser considerado (somente serão validados atributos vinculados com essas interfaces) 
				,ValidacaoAlteracao.class
			);
		 
		//Verificar se foram localizados erros
		if (listaConstraintViolationErrosValidacao != null && !listaConstraintViolationErrosValidacao.isEmpty()) {
			//Foram localizados erros. Preparar objeto a ser lançado junto à exceção com a lista de erros
			ErrosRequisicao errosRequisicao = new ErrosRequisicao();
			
			//Iterar pelos erros e popular o objeto a ser retornado junto à exceção
			for (ConstraintViolation<ProdutoDto> constraintViolationErroValidacao : listaConstraintViolationErrosValidacao) {
				errosRequisicao.getErros().add(new ErroProcessamento(null, constraintViolationErroValidacao.getMessage()));
			}
			
			throw new RequestInvalidoException("Requisição inválida", errosRequisicao, null);
		}
	}
}

