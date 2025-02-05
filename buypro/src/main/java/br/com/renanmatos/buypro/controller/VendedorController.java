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

import br.com.renanmatos.buypro.dto.VendedorDto;
import br.com.renanmatos.buypro.enuns.StatusVendedor;
import br.com.renanmatos.buypro.excecoes.ErroGenericoException;
import br.com.renanmatos.buypro.excecoes.RegistroJaExisteException;
import br.com.renanmatos.buypro.excecoes.RegistroNaoEncontradoException;
import br.com.renanmatos.buypro.excecoes.RequestInvalidoException;
import br.com.renanmatos.buypro.model.Vendedor;
import br.com.renanmatos.buypro.service.VendedorService;

@Controller
//Prefixo da URL de todos os serviços dessa classe
@RequestMapping("/vendedores")
public class VendedorController {

	//Objeto referente a logs
	private static final Log logger = LogFactory.getLog(VendedorController.class);
	
	//Injeção de dependência
	@Autowired
	private VendedorService vendedorService;
		
	
	
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
	public ResponseEntity<VendedorDto> getVendedorPorID(
		//Indica que o Spring deverá injetar o parâmetro de URL indicado
		@PathVariable("id") long idVendedor
	) throws ErroGenericoException {
		try {
			//Recuperar o vendedor da base
			Vendedor vendedor = vendedorService.consultarVendedorPorId(idVendedor, false);
			
			//Verificar se o registro foi localizado
			if (vendedor != null) {
				//Registro encontrado na base
				//Converter o objeto Vendedor para VendedorDto
				VendedorDto vendedorDto = vendedorService.getVendedorDtoPorVendedor(vendedor);

				//Retornar os registros juntamente com o código HTTP indicando sucesso
				return new ResponseEntity<VendedorDto>(vendedorDto, HttpStatus.OK);
			}else{
				/*Nenhum registro encontrado, retornar conteúdo vazio, juntamente com o código HTTP desejado*/
				return new ResponseEntity<VendedorDto>(HttpStatus.NOT_FOUND);
			}
		}catch(Exception e) {
			//Inserir a ocorrência do erro no arquivo de log
			logger.error("Erro ao recuperar o vendedor por idVendedor " + idVendedor + ": " + e.getMessage(), e);
			
			//Lançar a exceção para que seja tratada pelas pelos processos responsáveis
			throw new ErroGenericoException(e.getMessage(), null, e);
		}
	}
	
	// Serviço REST
		// Configurações do endpoint desse método
		@RequestMapping(
				// URL
				value = "/todosVendedores"
				// Método HTTP
				, method = RequestMethod.GET
				// Tipo do dado retornado
				, produces = { "application/json; charset=utf-8" })
		// ResponseEntity indica o conteúdo a ser retornado junto ao response HTTP
		public ResponseEntity<List<VendedorDto>> recuperarTodosVendedores() throws ErroGenericoException {
			try {
				// Recuperar todos os vendedores da base
				List<Vendedor> listaVendedores = vendedorService.carregarTodosVendedores(false);

				// Verificar se registros foram localizados
				if (listaVendedores != null && listaVendedores.size() > 0) {
					// Registros encontrados na base
					// Converter o objeto Vendedor para VendedorDto
					List<VendedorDto> listaVendedorDto = vendedorService.getListaVendedorDtoPorVendedor(listaVendedores);

					// Retornar os registros juntamente com o código HTTP indicando sucesso
					return new ResponseEntity<List<VendedorDto>>(listaVendedorDto, HttpStatus.OK);
				} else {
					/*
					 * Nenhum registro encontrado, retornar conteúdo vazio, juntamente com o código
					 * HTTP desejado
					 */
					return new ResponseEntity<List<VendedorDto>>(HttpStatus.NOT_FOUND);
				}
			} catch (Exception e) {
				// Inserir a ocorrência do erro no arquivo de log
				logger.error("Erro ao recuperar os vendedores: " + e.getMessage(), e);

				// Lançar a exceção para que seja tratada pelas pelos processos responsáveis
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
	public ResponseEntity<Void> salvarVendedor(
		//Payload recebido pela requisição
		@RequestBody 
		//Indicamos que os atributos do objeto deverão ser validados, lançando exceção de tipo MethodArgumentNotValidException em caso de falha
		//Caso desejar validar os campos por aqui
		//
		//@Validated(
		//	//Indicamos interface que indica o tipo de validação a ser realizada, onde SOMENTE atributos vinculados com essa interface serão validados
		//	ValidacaoCadastro.class
		//) 
		
		VendedorDto vendedorDto,
		//Objeto do Spring que permite configurar conteúdo retornado na requisição referente à URL
		UriComponentsBuilder uriComponentsBuilder
	) throws RequestInvalidoException, ErroGenericoException, RegistroJaExisteException {
		try {
			//Validar o preenchimento dos campos
			vendedorService.validarVendedorDtoParaCadastro(vendedorDto);

			//Converter o objeto DTO para Entidade
			Vendedor vendedor = vendedorService.getVendedorPorVendedorDto(vendedorDto);
			
			//Cadastrar o registro na base
			vendedor = vendedorService.salvarVendedor(vendedor);

			//Obter os headers da resposta da requisição
			HttpHeaders httpHeaders = new HttpHeaders();

			//Inserir no header "location", a URL que poderá ser acessada para obter o registro recem cadastrado, ficando algo do tipo: 						
			//http://localhost:8080/minhaAplicacao/vendedores/5        						
			httpHeaders.setLocation(uriComponentsBuilder.path("/vendedores/{id}").buildAndExpand(vendedor.getIdVendedor()).toUri());

			//Indicar os headers que devem ser incluídos ao retorno do serviço junto ao método HTTP indicando sucesso
			return new ResponseEntity<Void>(httpHeaders, HttpStatus.CREATED);
		}catch(RequestInvalidoException | RegistroJaExisteException e) {
			//Lançar a exceção para que seja tratada pelas pelos processos responsáveis
			throw e;
		}catch(Exception e) {
			//Inserir a ocorrência do erro no arquivo de log
			logger.error("Erro ao salvar o vendedor (" + vendedorDto.toString() + "): " + e.getMessage(), e);
			
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
	public ResponseEntity<Void> alterarVendedor(
		//Payload recebido pela requisição
		@RequestBody VendedorDto vendedorDto
	) throws Exception {
		try {
			//Validar o preenchimento dos campos
			vendedorService.validarVendedorDtoParaAlteracao(vendedorDto);

			//Converter o objeto DTO para Entidade
			Vendedor vendedor = vendedorService.getVendedorPorVendedorDto(vendedorDto);
			
			//Alterar o registro na base
			vendedor = vendedorService.alterarVendedor(vendedor);

			//Registro alterado na base, retornar o código HTTP indicando sucesso
			return new ResponseEntity<Void>( HttpStatus.OK);
		}catch(RequestInvalidoException | RegistroJaExisteException | RegistroNaoEncontradoException e) {
			//Lançar a exceção para que seja tratada pelas pelos processos responsáveis
			throw e;
		}catch(Exception e) {
			//Inserir a ocorrência do erro no arquivo de log
			logger.error("Erro ao alterar o vendedor (" + vendedorDto.toString() + "): " + e.getMessage(), e);
			
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
		,method=RequestMethod.DELETE
		//Tipo do dado retornado
		,produces = {"application/json; charset=utf-8"}
	)
	public ResponseEntity<Void> deletarVendedor(
		//Indica que o Spring deverá injetar o parâmetro de URL indicado
		@PathVariable("id") long idVendedor
	) throws ErroGenericoException, RegistroNaoEncontradoException {
		try {
			//Ao invés de deletar o registro, apenas alterar seu status para INATIVO 
			vendedorService.alterarStatusVendedor(idVendedor, StatusVendedor.INATIVO);
			
			/*Registro deletado na base, retornar tal registro juntamente com o código HTTP desejado*/
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}catch(RegistroNaoEncontradoException e) {
			//Lançar a exceção para que seja tratada pelas pelos processos responsáveis
			throw e; 
		}catch(Exception e) {
			//Inserir a ocorrência do erro no arquivo de log
			logger.error("Erro ao deletar o vendedor idVendedor " + idVendedor + ": " + e.getMessage(), e);
			
			//Lançar a exceção para que seja tratada pelas pelos processos responsáveis
			throw new ErroGenericoException(e.getMessage(), null, e);
		}
	}
}
