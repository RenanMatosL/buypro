package br.com.renanmatos.buypro.model;
import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

//Indica que se trata de entidade JPA
@Entity
//Configurações da tabela
@Table(
	//Nome da tabela
	name="PROFISSAO"
)
public class Profissao implements Serializable{

	//Atributos com anotações JPA
	
	//Indica se tratar de chave primária
	@Id
	//Indique que o valor da chave primária deve ser gerado pelo próprio banco de dados de maneira sequencial
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(
		//Nome da coluna na base de dados
		name="ID_PROFISSAO"
	)
	private Long idProfissao;
	
	@Column(
		//Nome da coluna na base de dados
		name = "NOME", 
		//Quantidade máxima de caracteres da coluna
		length = 250,
 		//Indica se a coluna pode possuir valores NULL
		nullable = false,
		//Indica se a coluna é de valores ÚNICOS (não podendo haver mais de uma linha com o mesmo valor)
		unique=true
	)
	private String nome;

	public Long getIdProfissao() {
		return idProfissao;
	}

	public void setIdProfissao(Long idProfissao) {
		this.idProfissao = idProfissao;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public int hashCode() {
		return Objects.hash(idProfissao);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Profissao other = (Profissao) obj;
		return Objects.equals(idProfissao, other.idProfissao);
	}


	
}
