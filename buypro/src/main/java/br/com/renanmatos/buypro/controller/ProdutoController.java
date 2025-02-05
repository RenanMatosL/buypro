package br.com.renanmatos.buypro.controller;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.renanmatos.buypro.dto.ProdutoDto;
import br.com.renanmatos.buypro.excecoes.ErroGenericoException;
import br.com.renanmatos.buypro.excecoes.RegistroJaExisteException;
import br.com.renanmatos.buypro.excecoes.RegistroNaoEncontradoException;
import br.com.renanmatos.buypro.excecoes.RequestInvalidoException;
import br.com.renanmatos.buypro.model.Produto;
import br.com.renanmatos.buypro.service.ProdutoService;

@Controller
//Prefixo da URL de todos os serviços dessa classe
@RequestMapping("/produtos")
public class ProdutoController {

	//Objeto referente a logs
	private static final Log logger = LogFactory.getLog(ProdutoController.class);
	
	//Injeção de dependência
	@Autowired
	private ProdutoService produtoService;
		
	//Serviço REST
	//Configurações do endpoint desse método
	@RequestMapping(
		//URL
		value="/todosProdutos"
		//Método HTTP
		,method=RequestMethod.GET
		//Tipo do dado retornado
		,produces = {"application/json; charset=utf-8"}
	)
	//ResponseEntity indica o conteúdo a ser retornado junto ao response HTTP
	public ResponseEntity<List<ProdutoDto>> recuperarTodosProdutos() throws ErroGenericoException {
		try {
			//Recuperar todos os clientes da base
			List<Produto> listaProdutos = produtoService.carregarTodosProdutos();
			
			//Verificar se registros foram localizados
			if (listaProdutos != null && listaProdutos.size() > 0) {
				//Registros encontrados na base
				//Converter o objeto Produto para ProdutoDto
				List<ProdutoDto> listaProdutoDto = produtoService.getListaProdutoDtoPorProduto(listaProdutos);
	
				//Retornar os registros juntamente com o código HTTP indicando sucesso
				return new ResponseEntity<List<ProdutoDto>>(listaProdutoDto, HttpStatus.OK);
			}else {
				/*Nenhum registro encontrado, retornar conteúdo vazio, juntamente com o código HTTP desejado*/
				return new ResponseEntity<List<ProdutoDto>>(HttpStatus.NOT_FOUND);
			}
		}catch(Exception e) {
			//Inserir a ocorrência do erro no arquivo de log
			logger.error("Erro ao recuperar os produtos: " + e.getMessage(), e);
			
			//Lançar a exceção para que seja tratada pelas pelos processos responsáveis
			throw new ErroGenericoException(e.getMessage(), null, e);
		}
	}
	
	//Serviço REST
	//Configurações do endpoint desse método
	@RequestMapping(
		//URL com parâmetro (injetado pelo Spring no parâmetro do método anotado com @PathVariable)
		value="/{id}"
		//Método HTTP
		,method=RequestMethod.GET
		//Tipo do dado retornado
		,produces = {"application/json; charset=utf-8"}
	)
	public ResponseEntity<ProdutoDto> getProdutoPorID(
		//Indica que o Spring deverá injetar o parâmetro de URL indicado
		@PathVariable("id") long idProduto
	) throws ErroGenericoException {
		try {
			//Recuperar o cliente da base
			Produto produto = produtoService.consultarProdutoPorId(idProduto);
			
			//Verificar se o registro foi localizado
			if (produto!= null) {
				//Registro encontrado na base
				//Converter o objeto Produto para ProdutoDto
				ProdutoDto produtoDto = produtoService.getProdutoDtoPorProduto(produto);

				//Retornar os registros juntamente com o código HTTP indicando sucesso
				return new ResponseEntity<ProdutoDto>(produtoDto, HttpStatus.OK);
			}else{
				/*Nenhum registro encontrado, retornar conteúdo vazio, juntamente com o código HTTP desejado*/
				return new ResponseEntity<ProdutoDto>(HttpStatus.NOT_FOUND);
			}
		}catch(Exception e) {
			//Inserir a ocorrência do erro no arquivo de log
			logger.error("Erro ao recuperar o produto por idProduto " + idProduto + ": " + e.getMessage(), e);
			
			//Lançar a exceção para que seja tratada pelas pelos processos responsáveis
			throw new ErroGenericoException(e.getMessage(), null, e);
		}
	}
	
	//Serviço REST
	//Configurações do endpoint desse método
	@RequestMapping(
		//URL
		value="/"
		//Método HTTP
		,method=RequestMethod.POST
		//Tipo do dado retornado
		,produces = {"application/json; charset=utf-8"}
		//Tipo de dado recebido
		,consumes = {"application/json; charset=utf-8"}
	)
	public ResponseEntity<Void> salvarProduto (
		//Payload recebido pela requisição
		@RequestBody ProdutoDto produtoDto, 
		//Objeto do Spring que permite configurar conteúdo retornado na requisição referente à URL
		UriComponentsBuilder uriComponentsBuilder
	) throws RequestInvalidoException, ErroGenericoException, RegistroJaExisteException {
		try {
			//Validar o preenchimento dos campos
			produtoService.validarProdutoDtoParaCadastro(produtoDto);

			//Converter o objeto DTO para Entidade
			Produto produto = produtoService.getProdutoPorProdutoDto(produtoDto);
			
			//Cadastrar o registro na base
			produto = produtoService.salvarProduto(produto);

			//Obter os headers da resposta da requisição
			HttpHeaders httpHeaders = new HttpHeaders();

			/*Inserir no header "location", a URL que poderá ser acessada para obter o registro recem cadastrado, ficando algo do tipo: 						
			http://localhost:8080/minhaAplicacao/produtos/5*/        						
			httpHeaders.setLocation(uriComponentsBuilder.path("/produtos/{id}").buildAndExpand(produto.getIdProduto()).toUri());

			/*Indicar os headers que devem ser incluídos ao retorno do serviço junto ao método HTTP indicando sucesso*/
			return new ResponseEntity<Void>(httpHeaders, HttpStatus.CREATED);
		}catch(RequestInvalidoException e) {
			//Lançar a exceção para que seja tratada pelas pelos processos responsáveis
			throw e;
		}catch(Exception e) {
			//Inserir a ocorrência do erro no arquivo de log
			logger.error("Erro ao salvar o produto (" + produtoDto.toString() + "): " + e.getMessage(), e);
			
			//Lançar a exceção para que seja tratada pelas pelos processos responsáveis
			throw new ErroGenericoException(e.getMessage(), null, e);
		}
	}
	
	//Serviço REST
	//Configurações do endpoint desse método
	@RequestMapping(
		//URL
		value="/"
		//Método HTTP
		,method=RequestMethod.PUT
		//Tipo do dado retornado
		,produces = {"application/json; charset=utf-8"}
		//Tipo de dado recebido
		,consumes = {"application/json; charset=utf-8"}
	)
	public ResponseEntity<Void> alterarProduto(
		//Payload recebido pela requisição
		@RequestBody ProdutoDto produtoDto
	) throws Exception {
		try {
			//Validar o preenchimento dos campos
			produtoService.validarProdutoDtoParaAlteracao(produtoDto);

			//Converter o objeto DTO para Entidade
			Produto produto = produtoService.getProdutoPorProdutoDto(produtoDto);
			
			//Alterar o registro na base
			produto = produtoService.alterarProduto(produto);

			//Registro alterado na base, retornar o código HTTP indicando sucesso
			return new ResponseEntity<Void>( HttpStatus.OK);
		}catch(RequestInvalidoException | RegistroNaoEncontradoException e) {
			//Lançar a exceção para que seja tratada pelas pelos processos responsáveis
			throw e;
		}catch(Exception e) {
			//Inserir a ocorrência do erro no arquivo de log
			logger.error("Erro ao alterar o produto (" + produtoDto.toString() + "): " + e.getMessage(), e);
			
			//Lançar a exceção para que seja tratada pelas pelos processos responsáveis
			throw new ErroGenericoException(e.getMessage(), null, e);
		}
	}
}

