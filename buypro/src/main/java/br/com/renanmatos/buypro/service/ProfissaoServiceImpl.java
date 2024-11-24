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

import br.com.renanmatos.buypro.dao.ProfissaoDao;
import br.com.renanmatos.buypro.dto.ErroProcessamento;
import br.com.renanmatos.buypro.dto.ErrosRequisicao;
import br.com.renanmatos.buypro.dto.ProfissaoDto;
import br.com.renanmatos.buypro.excecoes.RegistroJaExisteException;
import br.com.renanmatos.buypro.excecoes.RegistroNaoEncontradoException;
import br.com.renanmatos.buypro.excecoes.RequestInvalidoException;
import br.com.renanmatos.buypro.model.Profissao;
import br.com.renanmatos.buypro.validacao.ValidacaoAlteracao;
import br.com.renanmatos.buypro.validacao.ValidacaoCadastro;
import jakarta.validation.ConstraintViolation;

@Service
public class ProfissaoServiceImpl implements ProfissaoService{

	//Objeto de log
	private static final Log logger = LogFactory.getLog(ProfissaoServiceImpl.class);

	//Injeção de dependência (será injetada classe que IMPLEMENTA essa interface)
	@Autowired
	private ProfissaoDao profissaoDao;

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
	//Método que retorna a um List de TODAS Profissoes
	public List<Profissao> carregarTodasProfissoes() {
		try{
			return profissaoDao.carregarTodasProfissoes();
		}catch(Exception e){
			//Logar o erro ocorrido
			logger.error("Erro ao consultar todas as profissões", e);

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
	//Método que retorna um Profissao por seu ID
	public Profissao consultarProfissaoPorId(Long idProfissao) {
		try{
			return profissaoDao.consultarProfissaoPorId(idProfissao);
		}catch(Exception e){
			//Logar o erro ocorrido
			logger.error("Erro ao consultar uma profissao por ID. ID_PROFISSAO " + idProfissao, e);

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
	//Método que cadastra um novo profissao
	public Profissao salvarProfissao(Profissao profissao) throws RegistroJaExisteException {
		try{
			//Cadastrar o profissão em base e retornar essa entidade atualizada (com o ID preenchido)
			return profissaoDao.salvarProfissao(profissao);
		}catch(RegistroJaExisteException e){
			throw e;
		}catch(Exception e){
			//Logar o erro ocorrido
			logger.error("Erro ao salvar a profissão. Nome profissao: " + profissao.getNome(), e);

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
	//Método que altera um profissao
	public Profissao alterarProfissao(Profissao profissao) throws RegistroNaoEncontradoException, RegistroJaExisteException {
		try{
			/*Como NEM todos os atributos da entidade estão liberados para alteração, recuperar a entidade com os dados ATUAIS da base e alterar somente os dados que 
			possibilitamos tais alterações*/
			
			//Recuperar a entidade da base com os dados atuais
			Profissao profissaoBase = profissaoDao.consultarProfissaoPorId(profissao.getIdProfissao());
			
			//Verificar se o registro foi localizado
			if (profissaoBase == null) {
				throw new RegistroNaoEncontradoException("Profissao de idProfissao " + profissao.getIdProfissao() + " não localizada");
			}
			
			//Alterar os atributos que possibilitamos tais alterações
			profissaoBase.setNome(profissao.getNome());
			
			//Alterar a entidade em base de dados
			return profissaoDao.alterarProfissao(profissaoBase);	
		}catch(RegistroJaExisteException e){
			throw e;
		}catch(RegistroNaoEncontradoException e){
			throw e;
		}catch(Exception e){
			//Logar o erro ocorrido
			logger.error("Erro ao alterar a profissão. ID_PROFISSAO " + profissao.getIdProfissao(), e);

			//Lançar a exceção original
			throw e;
		}
	}

	//Indica que o método é implementação de uma interface
	@Override
	//Método que recebe uma lista de Profissao e retorna lista de ProfissaoDto
	public List<ProfissaoDto> getListaProfissaoDtoPorProfissao(List<Profissao> listaProfissao) {
		if (listaProfissao == null || listaProfissao.size() == 0) {
			return null;
		}else {
			List<ProfissaoDto> listaProfissaoDto = new ArrayList();
			
			for (Profissao profissao : listaProfissao) {
				ProfissaoDto profissaoDto = getProfissaoDtoPorProfissao(profissao);
				listaProfissaoDto.add(profissaoDto);
			}
			
			return listaProfissaoDto;
		}
	}

	//Indica que o método é implementação de uma interface
	@Override
	//Método que recebe uma lista de Profissao e retorna lista de ProfissaoDto
	public List<Profissao> getListaProfissaoPorProfissaoDto(List<ProfissaoDto> listaProfissaoDto) {
		if (listaProfissaoDto == null || listaProfissaoDto.size() == 0) {
			return null;
		}else {
			List<Profissao> listaProfissao = new ArrayList();
			
			for (ProfissaoDto profissaoDto : listaProfissaoDto) {
				Profissao profissao = getProfissaoPorProfissaoDto(profissaoDto);
				listaProfissao.add(profissao);
			}
			
			return listaProfissao;
		}
	}
	
	//Indica que o método é implementação de uma interface
	@Override
	//Método que recebe um Profissao e retorna um ProfissaoDto
	public ProfissaoDto getProfissaoDtoPorProfissao(Profissao profissao) {
		if (profissao != null) {
			ProfissaoDto profissaoDto = new ProfissaoDto();
			profissaoDto.setIdProfissao(profissao.getIdProfissao());
			profissaoDto.setNome(profissao.getNome());
			
			return profissaoDto;
		}else {
			return null;
		}
	}

	//Indica que o método é implementação de uma interface
	@Override
	//Método que recebe um ProfissaoDto e retorna um Profissao
	public Profissao getProfissaoPorProfissaoDto(ProfissaoDto profissaoDto) {
		if (profissaoDto != null) {
			Profissao profissao = new Profissao();
			profissao.setIdProfissao(profissaoDto.getIdProfissao());
			profissao.setNome(profissaoDto.getNome());
			
			return profissao;
		}else {
			return null;
		}
	}

	//Indica que o método é implementação de uma interface
	@Override
	//Método que valida campos DTO para CADASTRO
	public void validarProfissaoDtoParaCadastro(ProfissaoDto profissaoDto) throws RequestInvalidoException {
		//Validar o registro e recuperar os erros localizados
		Set<ConstraintViolation<ProfissaoDto>> listaConstraintViolationErrosValidacao = 
			localValidatorFactoryBean.validate(
				//Objeto a ser validado
				profissaoDto
				//Interfaces que representam o evento/grupo de validação a ser considerado (somente serão validados atributos vinculados com essas interfaces) 
				,ValidacaoCadastro.class
			);
		 
		//Verificar se foram localizados erros
		if (listaConstraintViolationErrosValidacao != null && !listaConstraintViolationErrosValidacao.isEmpty()) {
			//Foram localizados erros. Preparar objeto a ser lançado junto à exceção com a lista de erros
			ErrosRequisicao errosRequisicao = new ErrosRequisicao();
			
			//Iterar pelos erros e popular o objeto a ser retornado junto à exceção
			for (ConstraintViolation<ProfissaoDto> constraintViolationErroValidacao : listaConstraintViolationErrosValidacao) {
				errosRequisicao.getErros().add(new ErroProcessamento(null, constraintViolationErroValidacao.getMessage()));
			}
			
			throw new RequestInvalidoException("Requisição inválida", errosRequisicao, null);
		}
	}
	
	//Indica que o método é implementação de uma interface
	@Override
	//Método que valida campos DTO para ALTERAÇÃO
	public void validarProfissaoDtoParaAlteracao(ProfissaoDto profissaoDto) throws RequestInvalidoException {
		//Validar o registro e recuperar os erros localizados
		Set<ConstraintViolation<ProfissaoDto>> listaConstraintViolationErrosValidacao = 
			localValidatorFactoryBean.validate(
				//Objeto a ser validado
				profissaoDto
				//Interfaces que representam o evento/grupo de validação a ser considerado (somente serão validados atributos vinculados com essas interfaces) 
				,ValidacaoAlteracao.class
			);
		 
		//Verificar se foram localizados erros
		if (listaConstraintViolationErrosValidacao != null && !listaConstraintViolationErrosValidacao.isEmpty()) {
			//Foram localizados erros. Preparar objeto a ser lançado junto à exceção com a lista de erros
			ErrosRequisicao errosRequisicao = new ErrosRequisicao();
			
			//Iterar pelos erros e popular o objeto a ser retornado junto à exceção
			for (ConstraintViolation<ProfissaoDto> constraintViolationErroValidacao : listaConstraintViolationErrosValidacao) {
				errosRequisicao.getErros().add(new ErroProcessamento(null, constraintViolationErroValidacao.getMessage()));
			}
			
			throw new RequestInvalidoException("Requisição inválida", errosRequisicao, null);
		}
	}
}
