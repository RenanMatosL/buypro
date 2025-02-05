package br.com.renanmatos.buypro.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import br.com.renanmatos.buypro.dao.ClienteDao;
import br.com.renanmatos.buypro.dto.ClienteDto;
import br.com.renanmatos.buypro.dto.ErroProcessamento;
import br.com.renanmatos.buypro.dto.ErrosRequisicao;
import br.com.renanmatos.buypro.dto.ProfissaoDto;
import br.com.renanmatos.buypro.enuns.StatusClienteAtivo;
import br.com.renanmatos.buypro.excecoes.RegistroJaExisteException;
import br.com.renanmatos.buypro.excecoes.RegistroNaoEncontradoException;
import br.com.renanmatos.buypro.excecoes.RequestInvalidoException;
import br.com.renanmatos.buypro.model.Cliente;
import br.com.renanmatos.buypro.model.Profissao;
import br.com.renanmatos.buypro.validacao.ValidacaoAlteracao;
import br.com.renanmatos.buypro.validacao.ValidacaoCadastro;
import jakarta.validation.ConstraintViolation;

@Service
public class ClienteServiceImpl implements ClienteService {

	// Objeto de log
	private static final Log logger = LogFactory.getLog(ClienteServiceImpl.class);

	// Injeção de dependência (será injetada classe que IMPLEMENTA essa interface)
	@Autowired
	private ClienteDao clienteDao;

	@Autowired
	private ProfissaoService profissaoService;

	// Injeção de dependência (será injetada classe que IMPLEMENTA essa interface)
	@Autowired
	/*
	 * Como temos MAIS de um candidato para essa injeção de dependência (mais de uma
	 * classe implementando essa interface), fazemos uso da anotação @Qualifier para
	 * indicar o nome do Bean da classe que implementa essa interface que desejamos
	 * injetar como dependência
	 */
	@Qualifier("logAplicacaoBaseDadosServiceImpl")
	private LogAplicacaoService logAplicacaoService;

	// Injeção de dependência de Bean Validador Bean Validation vinculado com o
	// MessageSource de nosso arquivo properties de mensagens
	@Autowired
	// Indicamos o nome do bean que desejamos injetar
	@Qualifier("localValidatorFactoryBeanPadrao")
	private LocalValidatorFactoryBean localValidatorFactoryBean;

	// Configuração de transação
	@Transactional(
			/*
			 * Indica ao Spring que o método não precisa ser executado com uma transação,
			 * onde se JÁ houver uma transação aberta, esse método fará uso dela, mas se NÃO
			 * houver uma transação aberta, o Spring NÃO deverá criar uma para executar esse
			 * método
			 */
			propagation = Propagation.SUPPORTS)
	// Indica que o método é implementação de uma interface
	@Override
	// Método que retorna a um List de TODOS Clientes
	public List<Cliente> carregarTodosClientes(
			// Indica se a relação de PEDIDOS deverá ser carregada (JOIN realizado), já que
			// por padrão tal relação é LAZY e não seria carregada
			boolean recuperarPedidos) {
		try {
			return clienteDao.carregarTodosClientes(recuperarPedidos);
		} catch (Exception e) {
			// Logar o erro ocorrido
			logger.error("Erro ao consultar todos os clientes", e);

			// Lançar a exceção original
			throw e;
		}
	}

	// Configuração de transação
	@Transactional(
			/*
			 * Indica ao Spring que o método não precisa ser executado com uma transação,
			 * onde se JÁ houver uma transação aberta, esse método fará uso dela, mas se NÃO
			 * houver uma transação aberta, o Spring NÃO deverá criar uma para executar esse
			 * método
			 */
			propagation = Propagation.SUPPORTS)
	// Indica que o método é implementação de uma interface
	@Override
	// Método que retorna um Cliente por seu ID
	public Cliente consultarClientePorId(Long idCliente,
			// Indica se a relação de PEDIDOS deverá ser carregada (JOIN realizado), já que
			// por padrão tal relação é LAZY e não seria carregada
			boolean recuperarPedidos) {
		try {
			return clienteDao.consultarClientePorId(idCliente, recuperarPedidos);
		} catch (Exception e) {
			// Logar o erro ocorrido
			logger.error("Erro ao consultar um cliente por ID. ID_CLIENTE " + idCliente, e);

			// Lançar a exceção original
			throw e;
		}
	}

	// Configuração de transação
	@Transactional(
			/*
			 * Indica ao Spring que o método deverá ser executado com transação aberta, onde
			 * se já houver uma transação aberta, deverá utilizar essa transação aberta (ao
			 * invés de criar nova), caso contrário, deverá criar uma nova transação
			 */
			propagation = Propagation.REQUIRED)
	// Indica que o método é implementação de uma interface
//	@Override
	// Método que cadastra um novo cliente
	public Cliente salvarCliente(Cliente cliente) throws RegistroJaExisteException {
		try {
			// Via NOVA transação, iremos salvar em log a tentativa de cadastro desse
			// cliente (antes de realizar de fato o cadastro)
			logAplicacaoService.salvarLogAplicacao("Tentativa de cadastro de cliente chamado " + cliente.getNome());

			// Indicar informações padrão para novos registros
			cliente.setDataCadastro(new Date());
			cliente.setStatusClienteAtivo(StatusClienteAtivo.ATIVO);

			// Cadastrar o cliente em base e retornar essa entidade atualizada (com o ID
			// preenchido)
			return clienteDao.salvarCliente(cliente);
		} catch (RegistroJaExisteException e) {
			throw e;
		} catch (Exception e) {
			// Logar o erro ocorrido
			logger.error("Erro ao salvar o cliente. Nome cliente: " + cliente.getNome(), e);

			// Lançar a exceção original
			throw e;
		}
	}

	// Configuração de transação
	@Transactional(
			/*
			 * Indica ao Spring que o método deverá ser executado com transação aberta, onde
			 * se já houver uma transação aberta, deverá utilizar essa transação aberta (ao
			 * invés de criar nova), caso contrário, deverá criar uma nova transação
			 */
			propagation = Propagation.REQUIRED)
	// Indica que o método é implementação de uma interface
	@Override
	// Método que altera um cliente
	public Cliente alterarCliente(Cliente cliente) throws RegistroNaoEncontradoException, RegistroJaExisteException {
		try {
			/*
			 * Como NEM todos os atributos da entidade estão liberados para alteração,
			 * recuperar a entidade com os dados ATUAIS da base e alterar somente os dados
			 * que possibilitamos tais alterações
			 */

			// Recuperar a entidade da base com os dados atuais
			Cliente clienteBase = clienteDao.consultarClientePorId(cliente.getIdCliente(), false);

			// Verificar se o registro foi localizado
			if (clienteBase == null) {
				throw new RegistroNaoEncontradoException(
						"Cliente de idCliente " + cliente.getIdCliente() + " não localizado");
			}

			// Alterar os atributos que possibilitamos tais alterações
			clienteBase.setNome(cliente.getNome());
			clienteBase.setCpf(cliente.getCpf());
			clienteBase.setDataNascimento(cliente.getDataNascimento());

			// Alterar a entidade em base de dados
			return clienteDao.alterarCliente(clienteBase);
		} catch (RegistroJaExisteException e) {
			throw e;
		} catch (RegistroNaoEncontradoException e) {
			throw e;
		} catch (Exception e) {
			// Logar o erro ocorrido
			logger.error("Erro ao alterar o cliente. ID_CLIENTE " + cliente.getIdCliente(), e);

			// Lançar a exceção original
			throw e;
		}
	}

//
//	// Configuração de transação
//	@Transactional(
//			/*
//			 * Indica ao Spring que o método deverá ser executado com transação aberta, onde
//			 * se já houver uma transação aberta, deverá utilizar essa transação aberta (ao
//			 * invés de criar nova), caso contrário, deverá criar uma nova transação
//			 */
//			propagation = Propagation.REQUIRED)
//	// Indica que o método é implementação de uma interface
//	@Override
//	// Método que altera o status do cliente
//	public void alterarStatusCliente(Long idCliente, StatusClienteAtivo statusClienteAtivo)
//			throws RegistroNaoEncontradoException {
//		try {
//			clienteDao.alterarStatusCliente(idCliente, statusClienteAtivo);
//		} catch (RegistroNaoEncontradoException e) {
//			throw e;
//		} catch (Exception e) {
//			// Logar o erro ocorrido
//			logger.error("Erro ao alterar status do cliente. ID_CLIENTE " + idCliente, e);
//
//			// Lançar a exceção original
//			throw e;
//		}
//	}
//
//	// Indica que o método é implementação de uma interface
	@Override
	// Método que recebe uma lista de Cliente e retorna lista de ClienteDto
	public List<ClienteDto> getListaClienteDtoPorCliente(List<Cliente> listaCliente) {
		if (listaCliente == null || listaCliente.size() == 0) {
			return null;
		} else {
			List<ClienteDto> listaClienteDto = new ArrayList();

			for (Cliente cliente : listaCliente) {
				ClienteDto clienteDto = getClienteDtoPorCliente(cliente);
				listaClienteDto.add(clienteDto);
			}

			return listaClienteDto;
		}
	}

	// Indica que o método é implementação de uma interface
	@Override
	// Método que recebe um Cliente e retorna um ClienteDto
	public ClienteDto getClienteDtoPorCliente(Cliente cliente) {
		if (cliente != null) {
			ClienteDto clienteDto = new ClienteDto();
			clienteDto.setIdCliente(cliente.getIdCliente());
			clienteDto.setNome(cliente.getNome());
			clienteDto.setCpf(cliente.getCpf());
			clienteDto.setDataCadastro(cliente.getDataCadastro());
			clienteDto.setDataNascimento(cliente.getDataNascimento());
			clienteDto.setStatusClienteAtivo(cliente.getStatusClienteAtivo());

			// Relacionamento
			if (cliente.getListaProfissoes() != null && cliente.getListaProfissoes().size() > 0) {
				List<ProfissaoDto> listaProdissaoDto = profissaoService
						.getListaProfissaoDtoPorProfissao(cliente.getListaProfissoes());
				clienteDto.setListaProfissoes(listaProdissaoDto);
			}

			return clienteDto;
		} else {
			return null;
		}
	}

	@Override
	public void alterarStatusCliente(Long idCliente, StatusClienteAtivo statusClienteAtivo)
			throws RegistroNaoEncontradoException {
		// TODO Auto-genserated method stub

	}

	// Indica que o método é implementação de uma interface
	@Override
	// Método que recebe um ClienteDto e retorna um Cliente
	public Cliente getClientePorClienteDto(ClienteDto clienteDto) {
		if (clienteDto != null) {
			Cliente cliente = new Cliente();
			cliente.setIdCliente(clienteDto.getIdCliente());
			cliente.setNome(clienteDto.getNome());
			cliente.setCpf(clienteDto.getCpf());
			cliente.setDataCadastro(clienteDto.getDataCadastro());
			cliente.setDataNascimento(clienteDto.getDataNascimento());
			cliente.setStatusClienteAtivo(clienteDto.getStatusClienteAtivo());

			// Relacionamento
			if (clienteDto.getListaProfissoes() != null && clienteDto.getListaProfissoes().size() > 0) {
				List<Profissao> listaProfissoes = profissaoService
						.getListaProfissaoPorProfissaoDto(clienteDto.getListaProfissoes());
				cliente.setListaProfissoes(listaProfissoes);
			}

			return cliente;
		} else {
			return null;
		}
	}

	// Indica que o método é implementação de uma interface
	@Override
	// Método que valida campos DTO para CADASTRO
	public void validarClienteDtoParaCadastro(ClienteDto clienteDto) throws RequestInvalidoException {
		// Validar o registro e recuperar os erros localizados
		Set<ConstraintViolation<ClienteDto>> listaConstraintViolationErrosValidacao = localValidatorFactoryBean
				.validate(
						// Objeto a ser validado
						clienteDto
						// Interfaces que representam o evento/grupo de validação a ser considerado
						// (somente serão validados atributos vinculados com essas interfaces)
						, ValidacaoCadastro.class);

		// Verificar se foram localizados erros nos atributos simples (não validamos
		// ainda relacionamentos
		if (listaConstraintViolationErrosValidacao != null && !listaConstraintViolationErrosValidacao.isEmpty()) {
			// Foram localizados erros. Preparar objeto a ser lançado junto à exceção com a
			// lista de erros
			ErrosRequisicao errosRequisicao = new ErrosRequisicao();

			// Iterar pelos erros e popular o objeto a ser retornado junto à exceção
			for (ConstraintViolation<ClienteDto> constraintViolationErroValidacao : listaConstraintViolationErrosValidacao) {
				errosRequisicao.getErros()
						.add(new ErroProcessamento(null, constraintViolationErroValidacao.getMessage()));
			}

			throw new RequestInvalidoException("Requisição inválida", errosRequisicao, null);
		} else {
			// Não foram localizados erros em atributos simples, agora iremos validar
			// relacionamentos

			// Verificar se o relacionamento está preenchido
			if (clienteDto.getListaProfissoes() != null && clienteDto.getListaProfissoes().size() > 0) {
				// Relacionamento preenchido, verificar se todos os elementos da lista se
				// encontram na base de dados
				for (ProfissaoDto profissaoDto : clienteDto.getListaProfissoes()) {
					if (profissaoService.consultarProfissaoPorId(profissaoDto.getIdProfissao()) == null) {
						ErrosRequisicao errosRequisicao = new ErrosRequisicao();
						errosRequisicao.getErros().add(new ErroProcessamento(null,
								"Profissão de idProfissao " + profissaoDto.getIdProfissao() + " não localizada"));

						throw new RequestInvalidoException("Requisição inválida", errosRequisicao, null);
					}
				}
			}
		}
	}

	// Indica que o método é implementação de uma interface
	@Override
	// Método que valida campos DTO para ALTERAÇÃO
	public void validarClienteDtoParaAlteracao(ClienteDto clienteDto) throws RequestInvalidoException {
		// Validar o registro e recuperar os erros localizados
		Set<ConstraintViolation<ClienteDto>> listaConstraintViolationErrosValidacao = localValidatorFactoryBean
				.validate(
						// Objeto a ser validado
						clienteDto
						// Interfaces que representam o evento/grupo de validação a ser considerado
						// (somente serão validados atributos vinculados com essas interfaces)
						, ValidacaoAlteracao.class);

		// Verificar se foram localizados erros nos atributos simples (não validamos
		// ainda relacionamentos
		if (listaConstraintViolationErrosValidacao != null && !listaConstraintViolationErrosValidacao.isEmpty()) {
			// Foram localizados erros. Preparar objeto a ser lançado junto à exceção com a
			// lista de erros
			ErrosRequisicao errosRequisicao = new ErrosRequisicao();

			// Iterar pelos erros e popular o objeto a ser retornado junto à exceção
			for (ConstraintViolation<ClienteDto> constraintViolationErroValidacao : listaConstraintViolationErrosValidacao) {
				errosRequisicao.getErros()
						.add(new ErroProcessamento(null, constraintViolationErroValidacao.getMessage()));
			}

			throw new RequestInvalidoException("Requisição inválida", errosRequisicao, null);
		} else {
			// Não foram localizados erros em atributos simples, agora iremos validar
			// relacionamentos

			// Verificar se o relacionamento está preenchido
			if (clienteDto.getListaProfissoes() != null && clienteDto.getListaProfissoes().size() > 0) {
				// Relacionamento preenchido, verificar se todos os elementos da lista se
				// encontram na base de dados
				for (ProfissaoDto profissaoDto : clienteDto.getListaProfissoes()) {
					if (profissaoService.consultarProfissaoPorId(profissaoDto.getIdProfissao()) == null) {
						ErrosRequisicao errosRequisicao = new ErrosRequisicao();
						errosRequisicao.getErros().add(new ErroProcessamento(null,
								"Profissão de idProfissao " + profissaoDto.getIdProfissao() + " não localizada"));

						throw new RequestInvalidoException("Requisição inválida", errosRequisicao, null);
					}
				}
			}
		}
	}
}
