package br.com.renanmatos.buypro.dao;
import java.util.List;

import br.com.renanmatos.buypro.excecoes.RegistroJaExisteException;
import br.com.renanmatos.buypro.excecoes.RegistroNaoEncontradoException;
import br.com.renanmatos.buypro.model.Profissao;

public interface ProfissaoDao{
	public List<Profissao> carregarTodasProfissoes();
	public Profissao consultarProfissaoPorId(Long idProfissao) ;
	public Profissao salvarProfissao(Profissao profissao) throws RegistroJaExisteException;
	public Profissao alterarProfissao(Profissao profissao) throws RegistroNaoEncontradoException, RegistroJaExisteException;
	public void validarCamposUnicos (Profissao profissao) throws RegistroJaExisteException;
}
