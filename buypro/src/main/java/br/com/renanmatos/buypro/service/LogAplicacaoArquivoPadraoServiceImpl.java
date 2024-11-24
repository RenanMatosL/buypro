package br.com.renanmatos.buypro.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@Service
public class LogAplicacaoArquivoPadraoServiceImpl implements LogAplicacaoService{

	//Objeto de log
	private static final Log logger = LogFactory.getLog(LogAplicacaoArquivoPadraoServiceImpl.class);

	//Indica que o método é implementação de uma interface
	@Override
	//Método que cadastra um novo log
	public void salvarLogAplicacao(String descricao){
		logger.info(descricao);
	}
}
