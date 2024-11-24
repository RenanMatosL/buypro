package br.com.renanmatos.buypro.controller;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.renanmatos.buypro.dto.ErroProcessamento;
import br.com.renanmatos.buypro.dto.ErrosRequisicao;
import br.com.renanmatos.buypro.excecoes.ErroGenericoException;
import br.com.renanmatos.buypro.excecoes.RegistroJaExisteException;
import br.com.renanmatos.buypro.excecoes.RegistroNaoEncontradoException;
import br.com.renanmatos.buypro.excecoes.RequestInvalidoException;

/*Anotação que indica que implementações de @InitBinder, @ModelAttribute e @ExceptionHandler deverão ser propagadas para TODOS os Controllers indicados em basePackages*/
@ControllerAdvice(
	//Indico os pacotes que possuem classes controllers que esse tratamento de exceções deverá ser aplicado
	basePackages = {"br.com.renanmatos.buypro.controller"}
)
public class ExceptionHandlerControllerAdvice extends ResponseEntityExceptionHandler {
	
	/*Anotação que indica o tratamento do tipo de Exception indicada, onde os Controllers cobertos por essa classe irão tratar tais exceptions aqui*/
	@ExceptionHandler({RegistroNaoEncontradoException.class})
	protected ResponseEntity<Object> handlerRegistroNaoEncontradoException(
		/*Indicamos de parâmetro a exception que iremos tratar ou a super classe da mesma (herança)*/
		Exception exception, 
		WebRequest webRequest) {

		//Obter via cast a exception do tipo RegistroNaoEncontradoException
		RegistroNaoEncontradoException registroNaoEncontradoException = (RegistroNaoEncontradoException) exception;

		//Indicar tipo de conteúdo no HttpHeaders, JSON no caso
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("Content-Type", "application/json; charset=utf-8");

		//Retornar a requisição em status 404 (NOT FOUND) com a descrição do registro não localizado
		return handleExceptionInternal(exception, registroNaoEncontradoException.getMessage(), httpHeaders, HttpStatus.NOT_FOUND, webRequest);
	}
	
	/*Anotação que indica o tratamento do tipo de Exception indicada, onde os Controllers cobertos por essa classe irão tratar tais exceptions aqui*/
	@ExceptionHandler({ RequestInvalidoException.class })
	protected ResponseEntity<Object> handlerRequestInvalidoException(
		/*Indicamos de parâmetro a exception que iremos tratar ou a super classe da mesma (herança)*/
		Exception exception, 
		WebRequest webRequest) {

		//Obter via cast a exception do tipo RequestInvalidoException
		RequestInvalidoException requestInvalidoException = (RequestInvalidoException) exception;

		//Indicar tipo de conteúdo no HttpHeaders, JSON no caso
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("Content-Type", "application/json; charset=utf-8");

		//Retornar a requisição status 400 (BAD REQUEST) com os dados referentes aos erros de validação
		return handleExceptionInternal(exception, requestInvalidoException.getErrosRequisicao(), httpHeaders, HttpStatus.BAD_REQUEST, webRequest);
	}
	
	/*Anotação que indica o tratamento do tipo de Exception indicada, onde os Controllers cobertos por essa classe irão tratar tais exceptions aqui*/
	@ExceptionHandler({RegistroJaExisteException.class})
	protected ResponseEntity<Object> handlerRegistroJaExisteException(
		/*Indicamos de parâmetro a exception que iremos tratar ou a super classe da mesma (herança)*/
		Exception exception, 
		WebRequest webRequest) {

		//Obter via cast a exception do tipo RegistroNaoEncontradoException
		RegistroJaExisteException registroJaExisteException = (RegistroJaExisteException) exception;

		//Indicar tipo de conteúdo no HttpHeaders, JSON no caso
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("Content-Type", "application/json; charset=utf-8");

		//Retornar a requisição em status 409 (CONFLICT) com a descrição do conflito
		return handleExceptionInternal(exception, registroJaExisteException.getMessage(), httpHeaders, HttpStatus.CONFLICT, webRequest);
	}
	
	/*Anotação que indica o tratamento do tipo de Exception indicada, onde os Controllers "cobertos" por essa classe irão tratar tais exceptions aqui*/
	@ExceptionHandler({ ErroGenericoException.class })
	protected ResponseEntity<Object> handlerGenericExceptionException(Exception exception, WebRequest webRequest) {
		
		//Obter via cast a exception do tipo RequestInvalidoException
		ErroGenericoException erroGenericoException = (ErroGenericoException) exception;
        
		//Indicar tipo de conteúdo no HttpHeaders, JSON no caso
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("Content-Type", "application/json; charset=utf-8");

		/*Retornar a requisição em status 500 (INTERNAL SERVER ERROR) com um texto genérico (não é boa prática retornar o erro desconhecido ocorrido pois podem haver 
		informações sensíveis*/
		return handleExceptionInternal(exception, "Ocorreu um erro", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR, webRequest);
	}

	/*Sobrescrita do método que trata exceções de validação de Campos, como por exemplo Bean Validation*/
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
		MethodArgumentNotValidException methodArgumentNotValidException, HttpHeaders httpHeaders, HttpStatusCode httpStatusCode, WebRequest webRequest
	) {
		//Recuperar os erros de validação
		List<FieldError> listaFieldError = methodArgumentNotValidException.getBindingResult().getFieldErrors();
		
		//Preparar objeto a ser lançado junto à exceção com a lista de erros
		ErrosRequisicao errosRequisicao = new ErrosRequisicao();
		
		//Iterar pelos erros e popular o objeto a ser retornado junto à exceção
		for (FieldError fieldError : listaFieldError) {
			errosRequisicao.getErros().add(new ErroProcessamento(null, fieldError.getDefaultMessage()));
		}
		
		//Indicar tipo de conteúdo no HttpHeaders, JSON no caso
		HttpHeaders httpHeadersRetorno = new HttpHeaders();
		httpHeadersRetorno.set("Content-Type", "application/json; charset=utf-8");

		//Retornar a requisição status 400 (BAD REQUEST) com os dados referentes aos erros de validação
		return handleExceptionInternal(methodArgumentNotValidException, errosRequisicao, httpHeadersRetorno, HttpStatus.BAD_REQUEST, webRequest);
	}
}
