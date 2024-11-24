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


//Anotação indicando que esta classe é um controlador Spring  
@Controller  
@RequestMapping("/produtos") // Mapeia o endpoint base "/produtos"  
public class ProdutoController {  

 // Cria um logger para registrar mensagens de log  
 private static final Log logger = LogFactory.getLog(ProdutoController.class);  
 
 // Injeção de dependência do serviço de produtos  
 @Autowired  
 private ProdutoService produtoService;  

 // Método para recuperar todos os produtos  
 @RequestMapping(value = "/todosProdutos", method = RequestMethod.GET, produces = {"application/json; charset=utf-8"})  
 public ResponseEntity<List<ProdutoDto>> recuperarTodosProdutos() throws ErroGenericoException {  
     try {  
         // Chama o serviço para carregar todos os produtos  
         List<Produto> listaProdutos = produtoService.carregarTodosProdutos();  

         // Verifica se a lista de produtos não está vazia  
         if (listaProdutos != null && !listaProdutos.isEmpty()) {  
             // Converte a lista de produtos para a lista de ProdutoDto  
             List<ProdutoDto> listaProdutoDto = produtoService.getListaProdutoDtoPorProduto(listaProdutos);  
             // Retorna a lista de ProdutoDto com status 200 (OK)  
             return new ResponseEntity<>(listaProdutoDto, HttpStatus.OK);  
         } else {  
             // Retorna status 404 (NOT FOUND) se não houver produtos  
             return new ResponseEntity<>(HttpStatus.NOT_FOUND);  
         }  
     } catch (Exception e) {  
         // Registra erro e lança uma exceção genérica em caso de falha  
         logger.error("Erro ao recuperar os produtos: " + e.getMessage(), e);  
         throw new ErroGenericoException(e.getMessage(), null, e);  
     }  
 }  

 // Método para recuperar um produto pelo ID  
 @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {"application/json; charset=utf-8"})  
 public ResponseEntity<ProdutoDto> getProdutoPorID(@PathVariable("id") long idProduto) throws ErroGenericoException {  
     try {  
         // Chama o serviço para consultar o produto pelo ID  
         Produto produto = produtoService.consultarProdutoPorId(idProduto);  
         // Verifica se o produto foi encontrado  
         if (produto != null) {  
             // Converte o produto encontrado para ProdutoDto  
             ProdutoDto produtoDto = produtoService.getProdutoDtoPorProduto(produto);  
             // Retorna o ProdutoDto com status 200 (OK)  
             return new ResponseEntity<>(produtoDto, HttpStatus.OK);  
         } else {  
             // Retorna status 404 (NOT FOUND) se o produto não for encontrado  
             return new ResponseEntity<>(HttpStatus.NOT_FOUND);  
         }  
     } catch (Exception e) {  
         // Registra erro e lança uma exceção genérica em caso de falha  
         logger.error("Erro ao recuperar o produto por idProduto " + idProduto + ": " + e.getMessage(), e);  
         throw new ErroGenericoException(e.getMessage(), null, e);  
     }  
 }  

 // Método para salvar um novo produto  
 @RequestMapping(value = "/", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"}, consumes = {"application/json; charset=utf-8"})  
 public ResponseEntity<Object> salvarProduto(@RequestBody ProdutoDto produtoDto, UriComponentsBuilder uriComponentsBuilder)   
         throws RequestInvalidoException, ErroGenericoException, RegistroJaExisteException {  
     
     try {  
         // Valida o ProdutoDto antes de cadastrá-lo  
         produtoService.validarProdutoDtoParaCadastro(produtoDto);  
         // Converte o ProdutoDto para a entidade Produto  
         Produto produto = produtoService.getProdutoPorProdutoDto(produtoDto);  
         // Salva o produto no banco de dados  
         produto = produtoService.salvarProduto(produto);  
         // Cria headers para a resposta com a localização do novo recurso  
         HttpHeaders httpHeaders = new HttpHeaders();  
         httpHeaders.setLocation(uriComponentsBuilder.path("/produtos/{id}").buildAndExpand(produto.getIdProduto()).toUri());  
         // Retorna status 201 (CREATED) com os headers  
         return ResponseEntity.status(HttpStatus.CREATED).body("O produto com o código " + produtoDto.getCodigo() + " foi cadastrado com "
         		+ "sucesso."); 
     } catch (RequestInvalidoException e) {  
         // Lança a exceção se os dados fornecidos forem inválidos  
         throw e; // Delega tratamento para a camada superior  
     } catch (RegistroJaExisteException e) {  
         // Registra aviso se houver tentativa de cadastrar um produto já existente  
         logger.warn("Tentativa de cadastrar produto existente: " + e.getMessage());  
         // Retorna status 409 (CONFLICT) junto com uma mensagem customizada  
         return ResponseEntity.status(HttpStatus.CONFLICT).body("O produto com o código " + produtoDto.getCodigo() + " já está cadastrado.");  
     } catch (Exception e) {  
         // Registra erro e lança uma exceção genérica em caso de falha  
         logger.error("Erro ao salvar o produto (" + produtoDto.toString() + "): " + e.getMessage(), e);  
         throw new ErroGenericoException(e.getMessage(), null, e);  
     }  
 }   

 // Método para alterar um produto existente  
 @RequestMapping(value = "/", method = RequestMethod.PUT, produces = {"application/json; charset=utf-8"}, consumes = {"application/json; charset=utf-8"})  
 public ResponseEntity<Void> alterarProduto(@RequestBody ProdutoDto produtoDto) throws Exception {  
     try {  
         // Valida o ProdutoDto antes da alteração  
         produtoService.validarProdutoDtoParaAlteracao(produtoDto);  
         // Converte o ProdutoDto para a entidade Produto  
         Produto produto = produtoService.getProdutoPorProdutoDto(produtoDto);  
         // Atualiza o produto no banco de dados  
         produtoService.alterarProduto(produto);  
         // Retorna status 200 (OK) se a alteração for bem-sucedida  
         return new ResponseEntity<>(HttpStatus.OK);  
     } catch (RequestInvalidoException | RegistroNaoEncontradoException e) {  
         // Lança a exceção se os dados fornecidos forem inválidos ou registro não encontrado  
         throw e; // Delega tratamento para a camada superior  
     } catch (Exception e) {  
         // Registra erro e lança uma exceção genérica em caso de falha  
         logger.error("Erro ao alterar o produto (" + produtoDto.toString() + "): " + e.getMessage(), e);  
         throw new ErroGenericoException(e.getMessage(), null, e);  
     }  
 }
 
 @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = {"application/json; charset=utf-8"})  
 public ResponseEntity<Object> deleteProduto(@PathVariable("id") long idProduto) throws ErroGenericoException {  
     try {  
         // Tenta excluir o produto pelo ID  
         boolean isRemoved = produtoService.excluirProduto(idProduto);  
         
         // Verifica se a exclusão foi bem-sucedida  
         if (isRemoved) {  
             // Retorna status 200 (OK) com uma mensagem de sucesso  
             return ResponseEntity.ok("O produto com o código " + idProduto + " foi deletado com sucesso.");  
         } else {  
             // Retorna status 404 (NOT FOUND) se o produto não for encontrado  
             return ResponseEntity.status(HttpStatus.NOT_FOUND)  
                                  .body("Produto com o código " + idProduto + " não encontrado.");  
         }  
     } catch (Exception e) {  
         // Registra erro e lança uma exceção genérica em caso de falha  
         logger.error("Erro ao excluir o produto com id " + idProduto + ": " + e.getMessage(), e);  
         throw new ErroGenericoException(e.getMessage(), null, e);  
     }  
 }  
}  


