package br.com.renanmatos.buypro.service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import br.com.renanmatos.buypro.dao.PedidoDao;
import br.com.renanmatos.buypro.dto.ErroProcessamento;
import br.com.renanmatos.buypro.dto.ErrosRequisicao;
import br.com.renanmatos.buypro.dto.ItenPedidoDto;
import br.com.renanmatos.buypro.dto.PedidoDto;
import br.com.renanmatos.buypro.enuns.StatusPedido;
import br.com.renanmatos.buypro.excecoes.RegistroNaoEncontradoException;
import br.com.renanmatos.buypro.excecoes.RequestInvalidoException;
import br.com.renanmatos.buypro.model.Pedido;
import br.com.renanmatos.buypro.model.ProdutoPedido;
import br.com.renanmatos.buypro.validacao.ValidacaoCadastroPedido;
import jakarta.validation.ConstraintViolation;

@Service
public class PedidoServiceImpl implements PedidoService{

	//Injeção de dependência (será injetada classe que IMPLEMENTA essa interface)
	@Autowired
	private PedidoDao pedidoDao;

	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ClienteService clienteService;
	
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
	//Método que retorna a um List de TODOS Pedidos
	public List<Pedido> carregarTodosPedidos() {
		return pedidoDao.carregarTodosPedidos();
	}

	//Configuração de transação
	@Transactional(
		/*Indica ao Spring que o método não precisa ser executado com uma transação, onde se JÁ houver uma transação aberta, esse método fará uso dela, mas se NÃO houver uma 
		transação aberta, o Spring NÃO deverá criar uma para executar esse método*/
		propagation=Propagation.SUPPORTS
	)
	//Método que retorna um Pedido por seu ID
	public Pedido consultarPedidoPorId(Long idPedido) {
		return pedidoDao.consultarPedidoPorId(idPedido);
	}

	//Configuração de transação
	@Transactional(
		/*Indica ao Spring que o método deverá ser executado com transação aberta, onde se já houver uma transação aberta, 
		deverá utilizar essa transação aberta (ao invés de criar nova), caso contrário, deverá criar uma nova transação*/
		propagation=Propagation.REQUIRED
	)
	//Método que cadastra um novo pedido
	public Pedido salvarPedido(Pedido pedido){
		//Preencher atributos padrão
		pedido.setDataPedido(new Date());
		pedido.setStatusPedido(StatusPedido.AGUARDANDO_PAGAMENTO);
		
		//Calcular valores do pedido (preencher atributos da entidade)
		calcularValorPedido(pedido);
		
		return pedidoDao.salvarPedido(pedido);
	}

	public void calcularValorPedido(Pedido pedido) {
		BigDecimal valorPedido = new BigDecimal("0");
		for (ProdutoPedido produtoPedido : pedido.getListaProdutoPedido()) {
			//Preencher o valor unitário do produto no pedido recuperando da entidade produto
			produtoPedido.setValorUnitario(produtoPedido.getProduto().getPreco()); 
			
			//Atualizar o valor total do pedido (valor unitário vezes quantidade)
			valorPedido = valorPedido.add(produtoPedido.getValorUnitario().multiply(produtoPedido.getQuantidade()));
		}
		
		//Atualizar o valor total do pedido
		pedido.setValor(valorPedido);
	}
	
	//Configuração de transação
	@Transactional(
		/*Indica ao Spring que o método deverá ser executado com transação aberta, onde se já houver uma transação aberta, 
		deverá utilizar essa transação aberta (ao invés de criar nova), caso contrário, deverá criar uma nova transação*/
		propagation=Propagation.REQUIRED
	)
	//Método que altera um pedido
	public void alterarPedido(Pedido pedido){
		pedidoDao.alterarPedido(pedido);
	}

	//Indica que o método é implementação de uma interface
	@Override
	//Método que recebe uma lista de Pedido e retorna lista de PedidoDto
	public List<PedidoDto> getListaPedidoDtoPorPedido(List<Pedido> listaPedido) {
		if (listaPedido == null || listaPedido.size() == 0) {
			return null;
		}else {
			List<PedidoDto> listaPedidoDto = new ArrayList();
			
			for (Pedido pedido : listaPedido) {
				/* Dentro do loop, o método getPedidoDtoPorPedido é chamado para cada objeto Pedido, convertendo-o em um PedidoDto. 
				a variável pedidoDto armazena o resultado dessa conversão. */
				PedidoDto pedidoDto = getPedidoDtoPorPedido(pedido);
				listaPedidoDto.add(pedidoDto);
			}
			
			return listaPedidoDto;
		}
	}
	
	//Indica que o método é implementação de uma interface
	@Override
	//Método que recebe um Pedido e retorna um PedidoDto
	public PedidoDto getPedidoDtoPorPedido(Pedido pedido) {
		if (pedido != null) {
			PedidoDto pedidoDto = new PedidoDto();
			pedidoDto.setIdPedido(pedido.getIdPedido());
			pedidoDto.setIdCliente(pedido.getCliente().getIdCliente());
			pedidoDto.setDataPedido(pedido.getDataPedido());
			pedidoDto.setValor(pedido.getValor());
			pedidoDto.setStatusPedido(pedido.getStatusPedido());
			
			//Lista de itens
			if (pedido.getListaProdutoPedido() != null && pedido.getListaProdutoPedido().size() > 0) {
				for (ProdutoPedido produtoPedido : pedido.getListaProdutoPedido()) {
					ItenPedidoDto itenPedidoDto = new ItenPedidoDto();
					itenPedidoDto.setProduto(produtoService.getProdutoDtoPorProduto(produtoPedido.getProduto()));
					itenPedidoDto.setQuantidade(produtoPedido.getQuantidade());
					itenPedidoDto.setValorUnitario(produtoPedido.getValorUnitario());
					
					pedidoDto.getListaItenPedido().add(itenPedidoDto);
				}
			}
			
			return  pedidoDto;
		}else {
			return null;
		}
	}

	//Indica que o método é implementação de uma interface
	@Override
	//Método que recebe um PedidoDto e retorna um Pedido
	public Pedido getPedidoPorPedidoDto(PedidoDto pedidoDto) {
		if (pedidoDto != null) {
			Pedido pedido = new Pedido();
			pedido.setIdPedido(pedidoDto.getIdPedido());
			pedido.setCliente(clienteService.consultarClientePorId(pedidoDto.getIdCliente(), false));
			pedido.setDataPedido(pedidoDto.getDataPedido());
			pedido.setStatusPedido(pedidoDto.getStatusPedido());
			pedido.setValor(pedidoDto.getValor());
			
			//Lista de itens
			if (pedidoDto.getListaItenPedido() != null && pedidoDto.getListaItenPedido().size() > 0) {
				for (ItenPedidoDto itenPedidoDto : pedidoDto.getListaItenPedido()) {
					ProdutoPedido produtoPedido = new ProdutoPedido();
					produtoPedido.setPedido(pedido);
					produtoPedido.setProduto(produtoService.consultarProdutoPorId(itenPedidoDto.getProduto().getIdProduto()));
					produtoPedido.setQuantidade(itenPedidoDto.getQuantidade());
					produtoPedido.setValorUnitario(itenPedidoDto.getValorUnitario());
					
					pedido.getListaProdutoPedido().add(produtoPedido);
				}
			}
			
			return pedido;
		}else {
			return null;
		}
	}

	//Indica que o método é implementação de uma interface
	@Override
	//Método que valida campos DTO para CADASTRO
	public void validarPedidoDtoParaCadastro(PedidoDto pedidoDto) throws RequestInvalidoException {
		//Validar o registro e recuperar os erros localizados
		Set<ConstraintViolation<PedidoDto>> listaConstraintViolationErrosValidacao = 
			localValidatorFactoryBean.validate(
				//Objeto a ser validado
				pedidoDto
				//Interfaces que representam o evento/grupo de validação a ser considerado (somente serão validados atributos vinculados com essas interfaces) 
				,ValidacaoCadastroPedido.class
			);
		 
		//Verificar se foram localizados erros
		if (listaConstraintViolationErrosValidacao != null && !listaConstraintViolationErrosValidacao.isEmpty()) {
			//Foram localizados erros. Preparar objeto a ser lançado junto à exceção com a lista de erros
			ErrosRequisicao errosRequisicao = new ErrosRequisicao();
			
			//Iterar pelos erros e popular o objeto a ser retornado junto à exceção
			for (ConstraintViolation<PedidoDto> constraintViolationErroValidacao : listaConstraintViolationErrosValidacao) {
				errosRequisicao.getErros().add(new ErroProcessamento(null, constraintViolationErroValidacao.getMessage()));
			}
			
			throw new RequestInvalidoException("Requisição inválida", errosRequisicao, null);
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
	//Método que altera o status do pedido
	public void alterarStatusPedido(Long idPedido, StatusPedido statusPedido) throws RegistroNaoEncontradoException {
		//SUA implementação para verificar se pela regra de negócio, o status ATUAL do pedido pode avançar para o NOVO status solicitado
		
		pedidoDao.alterarStatusPedido(idPedido, statusPedido);
	}
}

