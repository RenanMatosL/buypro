package br.com.renanmatos.buypro.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.renanmatos.buypro.dao.LogAplicacaoDao;

@Service
public class LogAplicacaoBaseDadosServiceImpl implements LogAplicacaoService{

	//Objeto de log
	private static final Log logger = LogFactory.getLog(LogAplicacaoBaseDadosServiceImpl.class);

	//Injeção de dependência (será injetada classe que IMPLEMENTA essa interface)
	@Autowired
	private LogAplicacaoDao logAplicacaoDao;

	//Configuração de transação
	@Transactional(
		/*Indica ao Spring que o método deverá ser executado via NOVA transação, que será aberta no início da execução desse método e encerrada (via COMMIT ou ROLLBACK) no 
		término da execução desse método. Reforçando que mesmo que já exista uma aberta, será criada NOVA exclusivamente para esse método*/
		propagation=Propagation.REQUIRES_NEW
	)
	//Indica que o método é implementação de uma interface
	@Override
	//Método que cadastra um novo log
	public void salvarLogAplicacao(String descricao){
		try{
			logAplicacaoDao.salvarLogAplicacao(descricao);
		}catch(Exception e){
			//Logar o erro ocorrido
			logger.error("Erro ao salvar o log", e);

			//Lançar a exceção original
			throw e;
		}
	}
}
