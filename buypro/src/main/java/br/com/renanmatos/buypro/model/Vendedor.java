package br.com.renanmatos.buypro.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import br.com.renanmatos.buypro.enuns.StatusVendedor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

//Indica que se trata de entidade JPA
@Entity
//Configurações da tabela
@Table(
	//Nome da tabela
	name="VENDEDOR"
)
public class Vendedor implements Serializable{

	//Atributos com anotações JPA
	
	//Indica se tratar de chave primária
	@Id
	//Indique que o valor da chave primária deve ser gerado pelo próprio banco de dados de maneira sequencial
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(
		//Nome da coluna na base de dados
		name="ID_VENDEDOR"
	)
	private Long idVendedor;
	
	@Column(
		//Nome da coluna na base de dados
		name = "NOME", 
		//Quantidade máxima de caracteres da coluna
		length = 250,
 		//Indica se a coluna pode possuir valores NULL
		nullable = false
	)
	private String nome;
	
	@Column(
		//Nome da coluna na base de dados
		name = "CPF", 
		//Quantidade máxima de caracteres da coluna
		length = 250,
 		//Indica se a coluna pode possuir valores NULL
		nullable = false,
		//Indica se a coluna é de valores ÚNICOS (não podendo haver mais de uma linha com o mesmo valor)
		unique=true
	)
	private String cpf;

	//Indica tipo de dados sendo DATA
	@Temporal(
		//Indica que o formato da data é SEM as horas
		TemporalType.DATE
	)
	@Column(
		//Nome da coluna na base de dados
		name="DATA_NASCIMENTO", 
		//Indica se a coluna pode possuir valores NULL
		nullable=false
	)
	private Date dataNascimento;

	//Indica tipo de dados sendo DATA
	@Temporal(
		//Indica que o formato deve ser DATA e HORA
		TemporalType.TIMESTAMP
	)
	@Column(
		//Nome da coluna na base de dados
		name="DATA_CADASTRO", 
		//Indica se a coluna pode possuir valores NULL
		nullable=false
	)
	private Date dataCadastro;

	//Indica que o valor a ser indicado na coluna deverá ser recuperado via Enum
	@Enumerated(
		//Indica que o valor NUMÉRICO do Enum deverá ser salvo na base de dados
		EnumType.ORDINAL
	)
	@Column(
		//Nome da coluna na base de dados
		name = "VENDEDOR_ATIVO", 
		//Indica se a coluna pode possuir valores NULL
		nullable=false
	)
	private StatusVendedor statusVendedor;
	
	/*Relacionamento UM para MUITOS para implementação de relacionamento MANY to MANY com colunas EXTRAS!*/
	@OneToMany(mappedBy = "vendedor",fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
    private List<ProdutosDoVendedor> produtosVendedor = new ArrayList();
	
	
	
	/*Métodos equals e hashcode*/
	@Override
	public int hashCode() {
		return Objects.hash(idVendedor);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vendedor other = (Vendedor) obj;
		return Objects.equals(idVendedor, other.idVendedor);
	}
	//Getters e setters

	public Long getIdVendedor() {
		return idVendedor;
	}

	public void setIdVendedor(Long idVendedor) {
		this.idVendedor = idVendedor;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public StatusVendedor getStatusVendedor() {
		return statusVendedor;
	}

	public void setStatusVendedorAtivo(StatusVendedor statusVendedorAtivo) {
		this.statusVendedor = statusVendedorAtivo;
	}


	

}