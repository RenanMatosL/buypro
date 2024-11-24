package br.com.renanmatos.buypro.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

//Indica que se trata de entidade JPA
@Entity
//Configurações da tabela
@Table(
	//Nome da tabela
	name="LOG_APLICACAO"
)
public class LogAplicacao implements Serializable{

	//Atributos com anotações JPA
	
	//Indica se tratar de chave primária
	@Id
	//Indique que o valor da chave primária deve ser gerado pelo próprio banco de dados de maneira sequencial
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(
		//Nome da coluna na base de dados
		name="ID_LOG_APLICACAO"
	)
	private Long idLogAplicacao;
	
	@Column(
		//Nome da coluna na base de dados
		name = "DESCRICAO", 
		//Quantidade máxima de caracteres da coluna
		length = 250,
 		//Indica se a coluna pode possuir valores NULL
		nullable = false
	)
	private String descricao;
	
	//Indica tipo de dados sendo DATA
	@Temporal(
		//Indica que o formato deve ser DATA e HORA
		TemporalType.TIMESTAMP
	)
	@Column(
		//Nome da coluna na base de dados
		name="DATA_LOG", 
		//Indica se a coluna pode possuir valores NULL
		nullable=false
	)
	private Date dataLog;

	//Construtores
	public LogAplicacao(){}

	public LogAplicacao(String descricao, Date dataLog){
		this.descricao = descricao;
		this.dataLog = dataLog;
	}

	@Override
	public int hashCode() {
		return Objects.hash(idLogAplicacao);
	}

	/* Métodos equals e hashcode com base no atributo idLog, pois o mesmo representa a chave primária dessa entidade. Ação necessária para o Java realizar corretamente comparações 
	entre objetos em embora seriam diferentes instâncias, se tratam do MESMO registro (pois possuem o mesmo ID identificador)*/
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LogAplicacao other = (LogAplicacao) obj;
		return Objects.equals(idLogAplicacao, other.idLogAplicacao);
	}


	//Getters e setters
	public Long getIdLogAplicacao() {
		return idLogAplicacao;
	}
	
	public void setIdLogAplicacao(Long idLogAplicacao) {
		this.idLogAplicacao = idLogAplicacao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public Date getDataLog() {
		return dataLog;
	}
	
	public void setDataLog(Date dataLog) {
		this.dataLog = dataLog;
	}
	
	
}
