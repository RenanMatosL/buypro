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

import br.com.renanmatos.buypro.dto.ClienteDto;
import br.com.renanmatos.buypro.enuns.StatusClienteAtivo;
import br.com.renanmatos.buypro.excecoes.ErroGenericoException;
import br.com.renanmatos.buypro.excecoes.RegistroJaExisteException;
import br.com.renanmatos.buypro.excecoes.RegistroNaoEncontradoException;
import br.com.renanmatos.buypro.excecoes.RequestInvalidoException;
import br.com.renanmatos.buypro.model.Cliente;
import br.com.renanmatos.buypro.service.ClienteService;

@Controller
//Prefixo da URL de todos os serviços dessa classe
@RequestMapping("/clientes")
public class ClienteController {

	//Objeto referente a logs
	private static final Log logger = LogFactory.getLog(ClienteController.class);
	
	//Injeção de dependência
	@Autowired
	private ClienteService clienteService;
		
	//Serviço REST
	//Configurações do endpoint desse método
	@RequestMapping(
		//URL
		value="/todosClientes"
		//Método HTTP
		,method=RequestMethod.GET
		//Tipo do dado retornado
		,produces = {"application/json; charset=utf-8"}
	)
	//ResponseEntity indica o conteúdo a ser retornado junto ao response HTTP
	public ResponseEntity<List<ClienteDto>> recuperarTodosClientes() throws ErroGenericoException {
		try {
			//Recuperar todos os clientes da base
			List<Cliente> listaClientes = clienteService.carregarTodosClientes(false);
			
			//Verificar se registros foram localizados
			if (listaClientes != null && listaClientes.size() > 0) {
				//Registros encontrados na base
				//Converter o objeto Cliente para ClienteDto
				List<ClienteDto> listaClienteDto = clienteService.getListaClienteDtoPorCliente(listaClientes);
	
				//Retornar os registros juntamente com o código HTTP indicando sucesso
				return new ResponseEntity<List<ClienteDto>>(listaClienteDto, HttpStatus.OK);
			}else {
				/*Nenhum registro encontrado, retornar conteúdo vazio, juntamente com o código HTTP desejado*/
				return new ResponseEntity<List<ClienteDto>>(HttpStatus.NOT_FOUND);
			}
		}catch(Exception e) {
			//Inserir a ocorrência do erro no arquivo de log
			logger.error("Erro ao recuperar os clientes: " + e.getMessage(), e);
			
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
	public ResponseEntity<ClienteDto> getClientePorID(
		//Indica que o Spring deverá injetar o parâmetro de URL indicado
		@PathVariable("id") long idCliente
	) throws ErroGenericoException {
		try {
			//Recuperar o cliente da base
			Cliente cliente = clienteService.consultarClientePorId(idCliente, false);
			
			//Verificar se o registro foi localizado
			if (cliente != null) {
				//Registro encontrado na base
				//Converter o objeto Cliente para ClienteDto
				ClienteDto clienteDto = clienteService.getClienteDtoPorCliente(cliente);

				//Retornar os registros juntamente com o código HTTP indicando sucesso
				return new ResponseEntity<ClienteDto>(clienteDto, HttpStatus.OK);
			}else{
				/*Nenhum registro encontrado, retornar conteúdo vazio, juntamente com o código HTTP desejado*/
				return new ResponseEntity<ClienteDto>(HttpStatus.NOT_FOUND);
			}
		}catch(Exception e) {
			//Inserir a ocorrência do erro no arquivo de log
			logger.error("Erro ao recuperar o cliente por idCliente " + idCliente + ": " + e.getMessage(), e);
			
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
	public ResponseEntity<Void> salvarCliente(
		//Payload recebido pela requisição
		@RequestBody 
		//Indicamos que os atributos do objeto deverão ser validados, lançando exceção de tipo MethodArgumentNotValidException em caso de falha
		//Caso desejar validar os campos por aqui
		//
		//@Validated(
		//	//Indicamos interface que indica o tipo de validação a ser realizada, onde SOMENTE atributos vinculados com essa interface serão validados
		//	ValidacaoCadastro.class
		//) 
		
		ClienteDto clienteDto,
		//Objeto do Spring que permite configurar conteúdo retornado na requisição referente à URL
		UriComponentsBuilder uriComponentsBuilder
	) throws RequestInvalidoException, ErroGenericoException, RegistroJaExisteException {
		try {
			//Validar o preenchimento dos campos
			clienteService.validarClienteDtoParaCadastro(clienteDto);

			//Converter o objeto DTO para Entidade
			Cliente cliente = clienteService.getClientePorClienteDto(clienteDto);
			
			//Cadastrar o registro na base
			cliente = clienteService.salvarCliente(cliente);

			//Obter os headers da resposta da requisição
			HttpHeaders httpHeaders = new HttpHeaders();

			//Inserir no header "location", a URL que poderá ser acessada para obter o registro recem cadastrado, ficando algo do tipo: 						
			//http://localhost:8080/minhaAplicacao/clientes/5        						
			httpHeaders.setLocation(uriComponentsBuilder.path("/clientes/{id}").buildAndExpand(cliente.getIdCliente()).toUri());

			//Indicar os headers que devem ser incluídos ao retorno do serviço junto ao método HTTP indicando sucesso
			return new ResponseEntity<Void>(httpHeaders, HttpStatus.CREATED);
		}catch(RequestInvalidoException | RegistroJaExisteException e) {
			//Lançar a exceção para que seja tratada pelas pelos processos responsáveis
			throw e;
		}catch(Exception e) {
			//Inserir a ocorrência do erro no arquivo de log
			logger.error("Erro ao salvar o cliente (" + clienteDto.toString() + "): " + e.getMessage(), e);
			
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
	public ResponseEntity<Void> alterarCliente(
		//Payload recebido pela requisição
		@RequestBody ClienteDto clienteDto
	) throws Exception {
		try {
			//Validar o preenchimento dos campos
			clienteService.validarClienteDtoParaAlteracao(clienteDto);

			//Converter o objeto DTO para Entidade
			Cliente cliente = clienteService.getClientePorClienteDto(clienteDto);
			
			//Alterar o registro na base
			cliente = clienteService.alterarCliente(cliente);

			//Registro alterado na base, retornar o código HTTP indicando sucesso
			return new ResponseEntity<Void>( HttpStatus.OK);
		}catch(RequestInvalidoException | RegistroJaExisteException | RegistroNaoEncontradoException e) {
			//Lançar a exceção para que seja tratada pelas pelos processos responsáveis
			throw e;
		}catch(Exception e) {
			//Inserir a ocorrência do erro no arquivo de log
			logger.error("Erro ao alterar o cliente (" + clienteDto.toString() + "): " + e.getMessage(), e);
			
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
	public ResponseEntity<Void> deletarCliente(
		//Indica que o Spring deverá injetar o parâmetro de URL indicado
		@PathVariable("id") long idCliente
	) throws ErroGenericoException, RegistroNaoEncontradoException {
		try {
			//Ao invés de deletar o registro, apenas alterar seu status para INATIVO 
			clienteService.alterarStatusCliente(idCliente, StatusClienteAtivo.INATIVO);
			
			/*Registro deletado na base, retornar tal registro juntamente com o código HTTP desejado*/
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}catch(RegistroNaoEncontradoException e) {
			//Lançar a exceção para que seja tratada pelas pelos processos responsáveis
			throw e;
		}catch(Exception e) {
			//Inserir a ocorrência do erro no arquivo de log
			logger.error("Erro ao deletar o cliente idCliente " + idCliente + ": " + e.getMessage(), e);
			
			//Lançar a exceção para que seja tratada pelas pelos processos responsáveis
			throw new ErroGenericoException(e.getMessage(), null, e);
		}
	}
}
