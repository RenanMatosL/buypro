package br.com.renanmatos.buypro.service;
import java.util.List;

import br.com.renanmatos.buypro.dto.ProfissaoDto;
import br.com.renanmatos.buypro.excecoes.RegistroJaExisteException;
import br.com.renanmatos.buypro.excecoes.RegistroNaoEncontradoException;
import br.com.renanmatos.buypro.excecoes.RequestInvalidoException;
import br.com.renanmatos.buypro.model.Profissao;

public interface ProfissaoService{
	public List<Profissao> carregarTodasProfissoes();
	public Profissao consultarProfissaoPorId(Long idProfissao) ;
	public Profissao salvarProfissao(Profissao profissao) throws RegistroJaExisteException;
	public Profissao alterarProfissao(Profissao profissao) throws RegistroNaoEncontradoException, RegistroJaExisteException;
	public List<ProfissaoDto> getListaProfissaoDtoPorProfissao(List<Profissao> listaProfissao);
	public List<Profissao> getListaProfissaoPorProfissaoDto(List<ProfissaoDto> listaProfissaoDto);
	public ProfissaoDto getProfissaoDtoPorProfissao(Profissao profissao);
	public Profissao getProfissaoPorProfissaoDto(ProfissaoDto profissaoDto);
	public void validarProfissaoDtoParaCadastro(ProfissaoDto profissaoDto) throws RequestInvalidoException;
	public void validarProfissaoDtoParaAlteracao(ProfissaoDto profissaoDto) throws RequestInvalidoException;
}
