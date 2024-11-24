package br.com.renanmatos.buypro.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.renanmatos.buypro.dto.ProfissaoDto;
import br.com.renanmatos.buypro.excecoes.ErroGenericoException;
import br.com.renanmatos.buypro.excecoes.RegistroJaExisteException;
import br.com.renanmatos.buypro.excecoes.RegistroNaoEncontradoException;
import br.com.renanmatos.buypro.excecoes.RequestInvalidoException;
import br.com.renanmatos.buypro.model.Profissao;
import br.com.renanmatos.buypro.validacao.ValidacaoCadastro;

@Controller
//Prefixo da URL de todos os serviços dessa classe
@RequestMapping("/profissoes")
public class ProfissaoController {

	//Objeto referente a logs
	private static final Log logger = LogFactory.getLog(ProfissaoController.class);
	
	//Injeção de dependência
	@Autowired
	private ProfissaoService profissaoService;
		
	//Serviço REST
	//Configurações do endpoint desse método
	@RequestMapping(
		//URL
		value="/todasProfissoes"
		//Método HTTP
		,method=RequestMethod.GET
		//Tipo do dado retornado
		,produces = {"application/json; charset=utf-8"}
	)
	//ResponseEntity indica o conteúdo a ser retornado junto ao response HTTP
	public ResponseEntity<List<ProfissaoDto>> recuperarTodasProfissoes() throws ErroGenericoException {
		try {
			//Recuperar todos os profissoes da base
			List<Profissao> listaProfissoes = profissaoService.carregarTodasProfissoes();
			
			//Verificar se registros foram localizados
			if (listaProfissoes != null && listaProfissoes.size() > 0) {
				//Registros encontrados na base
				//Converter o objeto Profissao para ProfissaoDto
				List<ProfissaoDto> listaProfissaoDto = profissaoService.getListaProfissaoDtoPorProfissao(listaProfissoes);
	
				//Retornar os registros juntamente com o código HTTP indicando sucesso
				return new ResponseEntity<List<ProfissaoDto>>(listaProfissaoDto, HttpStatus.OK);
			}else {
				/*Nenhum registro encontrado, retornar conteúdo vazio, juntamente com o código HTTP desejado*/
				return new ResponseEntity<List<ProfissaoDto>>(HttpStatus.NOT_FOUND);
			}
		}catch(Exception e) {
			//Inserir a ocorrência do erro no arquivo de log
			logger.error("Erro ao recuperar os profissoes: " + e.getMessage(), e);
			
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
	public ResponseEntity<ProfissaoDto> getProfissaoPorID(
		//Indica que o Spring deverá injetar o parâmetro de URL indicado
		@PathVariable("id") long idProfissao
	) throws ErroGenericoException {
		try {
			//Recuperar o profissao da base
			Profissao profissao = profissaoService.consultarProfissaoPorId(idProfissao);
			
			//Verificar se o registro foi localizado
			if (profissao != null) {
				//Registro encontrado na base
				//Converter o objeto Profissao para ProfissaoDto
				ProfissaoDto profissaoDto = profissaoService.getProfissaoDtoPorProfissao(profissao);

				//Retornar os registros juntamente com o código HTTP indicando sucesso
				return new ResponseEntity<ProfissaoDto>(profissaoDto, HttpStatus.OK);
			}else{
				/*Nenhum registro encontrado, retornar conteúdo vazio, juntamente com o código HTTP desejado*/
				return new ResponseEntity<ProfissaoDto>(HttpStatus.NOT_FOUND);
			}
		}catch(Exception e) {
			//Inserir a ocorrência do erro no arquivo de log
			logger.error("Erro ao recuperar a profissão por idProfissao " + idProfissao + ": " + e.getMessage(), e);
			
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
	public ResponseEntity<Void> salvarProfissao(
		//Payload recebido pela requisição
		@RequestBody 
		//Indicamos que os atributos do objeto deverão ser validados, lançando exceção de tipo MethodArgumentNotValidException em caso de falha
		@Validated(
			//Indicamos interface que indica o tipo de validação a ser realizada, onde SOMENTE atributos vinculados com essa interface serão validados
			ValidacaoCadastro.class
		) 
		ProfissaoDto profissaoDto,
		//Objeto do Spring que permite configurar conteúdo retornado na requisição referente à URL
		UriComponentsBuilder uriComponentsBuilder
	) throws RequestInvalidoException, ErroGenericoException, RegistroJaExisteException {
		try {
			//Validar o preenchimento dos campos
			//Método comentado pois nesse exemplo estamos realizando a validação via @Validated
			//profissaoService.validarProfissaoDtoParaCadastro(profissaoDto);

			//Converter o objeto DTO para Entidade
			Profissao profissao = profissaoService.getProfissaoPorProfissaoDto(profissaoDto);
			
			//Cadastrar o registro na base
			profissao = profissaoService.salvarProfissao(profissao);

			//Obter os headers da resposta da requisição
			HttpHeaders httpHeaders = new HttpHeaders();

			/*Inserir no header "location", a URL que poderá ser acessada para obter o registro recem cadastrado, ficando algo do tipo: 						
			http://localhost:8080/minhaAplicacao/profissoes/5*/        						
			httpHeaders.setLocation(uriComponentsBuilder.path("/profissoes/{id}").buildAndExpand(profissao.getIdProfissao()).toUri());

			/*Indicar os headers que devem ser incluídos ao retorno do serviço junto ao método HTTP indicando sucesso*/
			return new ResponseEntity<Void>(httpHeaders, HttpStatus.CREATED);
		}catch(/*RequestInvalidoException |*/ RegistroJaExisteException e) {
			//Lançar a exceção para que seja tratada pelas pelos processos responsáveis
			throw e;
		}catch(Exception e) {
			//Inserir a ocorrência do erro no arquivo de log
			logger.error("Erro ao salvar a profissão (" + profissaoDto.toString() + "): " + e.getMessage(), e);
			
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
	public ResponseEntity<Void> alterarProfissao(
		//Payload recebido pela requisição
		@RequestBody ProfissaoDto profissaoDto
	) throws Exception {
		try {
			//Validar o preenchimento dos campos
			profissaoService.validarProfissaoDtoParaAlteracao(profissaoDto);

			//Converter o objeto DTO para Entidade
			Profissao profissao = profissaoService.getProfissaoPorProfissaoDto(profissaoDto);
			
			//Alterar o registro na base
			profissao = profissaoService.alterarProfissao(profissao);

			//Registro alterado na base, retornar o código HTTP indicando sucesso
			return new ResponseEntity<Void>( HttpStatus.OK);
		}catch(RequestInvalidoException | RegistroJaExisteException | RegistroNaoEncontradoException e) {
			//Lançar a exceção para que seja tratada pelas pelos processos responsáveis
			throw e;
		}catch(Exception e) {
			//Inserir a ocorrência do erro no arquivo de log
			logger.error("Erro ao alterar a profissão (" + profissaoDto.toString() + "): " + e.getMessage(), e);
			
			//Lançar a exceção para que seja tratada pelas pelos processos responsáveis
			throw new ErroGenericoException(e.getMessage(), null, e);
		}
	}
}
