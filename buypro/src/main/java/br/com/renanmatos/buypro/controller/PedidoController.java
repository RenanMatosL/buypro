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

import br.com.renanmatos.buypro.dto.PedidoDto;
import br.com.renanmatos.buypro.excecoes.ErroGenericoException;
import br.com.renanmatos.buypro.excecoes.RegistroNaoEncontradoException;
import br.com.renanmatos.buypro.excecoes.RequestInvalidoException;
import br.com.renanmatos.buypro.model.Pedido;
import br.com.renanmatos.buypro.service.PedidoService;

@Controller
//Prefixo da URL de todos os serviços dessa classe
@RequestMapping("/pedidos")
public class PedidoController {

	//Objeto referente a logs
	private static final Log logger = LogFactory.getLog(PedidoController.class);
	
	//Injeção de dependência
	@Autowired
	private PedidoService pedidoService;
		
	//Serviço REST
	//Configurações do endpoint desse método
	@RequestMapping(
		//URL
		value="/todosPedidos"
		//Método HTTP
		,method=RequestMethod.GET
		//Tipo do dado retornado
		,produces = {"application/json; charset=utf-8"}
	)
	//ResponseEntity indica o conteúdo a ser retornado junto ao response HTTP
	public ResponseEntity<List<PedidoDto>> recuperarTodosPedidos() throws ErroGenericoException {
		try {
			//Recuperar todos os clientes da base
			List<Pedido> listaPedidos = pedidoService.carregarTodosPedidos();
			
			//Verificar se registros foram localizados
			if (listaPedidos != null && listaPedidos.size() > 0) {
				//Registros encontrados na base
				//Converter o objeto Produto para ProdutoDto
				List<PedidoDto> listaPedidoDto = pedidoService.getListaPedidoDtoPorPedido(listaPedidos);
	
				//Retornar os registros juntamente com o código HTTP indicando sucesso
				return new ResponseEntity<List<PedidoDto>>(listaPedidoDto, HttpStatus.OK);
			}else {
				/*Nenhum registro encontrado, retornar conteúdo vazio, juntamente com o código HTTP desejado*/
				return new ResponseEntity<List<PedidoDto>>(HttpStatus.NOT_FOUND);
			}
		}catch(Exception e) {
			//Inserir a ocorrência do erro no arquivo de log
			logger.error("Erro ao recuperar os pedidos: " + e.getMessage(), e);
			
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
	public ResponseEntity<PedidoDto> getPedidoPorID(
		//Indica que o Spring deverá injetar o parâmetro de URL indicado
		@PathVariable("id") long idPedido
	) throws ErroGenericoException {
		try {
			//Recuperar o cliente da base
			Pedido pedido = pedidoService.consultarPedidoPorId(idPedido);
			
			//Verificar se o registro foi localizado
			if (pedido!= null) {
				//Registro encontrado na base
				//Converter o objeto Produto para ProdutoDto
				PedidoDto pedidoDto = pedidoService.getPedidoDtoPorPedido(pedido);

				//Retornar os registros juntamente com o código HTTP indicando sucesso
				return new ResponseEntity<PedidoDto>(pedidoDto, HttpStatus.OK);
			}else{
				/*Nenhum registro encontrado, retornar conteúdo vazio, juntamente com o código HTTP desejado*/
				return new ResponseEntity<PedidoDto>(HttpStatus.NOT_FOUND);
			}
		}catch(Exception e) {
			//Inserir a ocorrência do erro no arquivo de log
			logger.error("Erro ao recuperar o pedido por idPedido " + idPedido + ": " + e.getMessage(), e);
			
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
	public ResponseEntity<Void> salvarPedido (
		//Payload recebido pela requisição
		@RequestBody PedidoDto pedidoDto, 
		//Objeto do Spring que permite configurar conteúdo retornado na requisição referente à URL
		UriComponentsBuilder uriComponentsBuilder
	) throws RequestInvalidoException, ErroGenericoException {
		try {
			//Validar o preenchimento dos campos
			pedidoService.validarPedidoDtoParaCadastro(pedidoDto);

			//Converter o objeto DTO para Entidade
			Pedido pedido = pedidoService.getPedidoPorPedidoDto(pedidoDto);
			
			//Cadastrar o registro na base
			pedido = pedidoService.salvarPedido(pedido);

			//Obter os headers da resposta da requisição
			HttpHeaders httpHeaders = new HttpHeaders();

			/*Inserir no header "location", a URL que poderá ser acessada para obter o registro recem cadastrado, ficando algo do tipo: 						
			http://localhost:8080/minhaAplicacao/produtos/5*/        						
			httpHeaders.setLocation(uriComponentsBuilder.path("/pedidos/{id}").buildAndExpand(pedido.getIdPedido()).toUri());

			/*Indicar os headers que devem ser incluídos ao retorno do serviço junto ao método HTTP indicando sucesso*/
			return new ResponseEntity<Void>(httpHeaders, HttpStatus.CREATED);
		}catch(RequestInvalidoException e) {
			//Lançar a exceção para que seja tratada pelos processos responsáveis
			throw e;
		}catch(Exception e) {
			//Inserir a ocorrência do erro no arquivo de log
			logger.error("Erro ao salvar o pedido (" + pedidoDto.toString() + "): " + e.getMessage(), e);
			
			//Lançar a exceção para que seja tratada pelas pelos processos responsáveis
			throw new ErroGenericoException(e.getMessage(), null, e);
		}
	}
	
	//Serviço REST
	//Configurações do endpoint desse método
	@RequestMapping(
		//URL
		value="/alterarStatusPedido"
		//Método HTTP
		,method=RequestMethod.PUT
		//Tipo do dado retornado
		,produces = {"application/json; charset=utf-8"}
		//Tipo de dado recebido
		,consumes = {"application/json; charset=utf-8"}
	)
	public ResponseEntity<Void> alterarStatusPedido(
		//Payload recebido pela requisição
		@RequestBody PedidoDto pedidoDto
	) throws Exception {
		try {
			//Alterar o registro na base
			pedidoService.alterarStatusPedido(pedidoDto.getIdPedido(), pedidoDto.getStatusPedido());

			//Registro alterado na base, retornar o código HTTP indicando sucesso
			return new ResponseEntity<Void>( HttpStatus.OK);
		}catch(RegistroNaoEncontradoException e) {
			//Lançar a exceção para que seja tratada pelas pelos processos responsáveis
			throw e;
		}catch(Exception e) {
			//Inserir a ocorrência do erro no arquivo de log
			logger.error("Erro ao alterar o pedido (" + pedidoDto.toString() + "): " + e.getMessage(), e);
			
			//Lançar a exceção para que seja tratada pelas pelos processos responsáveis
			throw new ErroGenericoException(e.getMessage(), null, e);
		}
	}
}
