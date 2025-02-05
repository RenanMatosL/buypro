package br.com.renanmatos.buypro.model;
import java.io.Serializable;
import java.math.BigDecimal;

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
	name="PRODUTO"
)
public class Produto implements Serializable{

	//Atributos com anotações JPA
	
	//Indica se tratar de chave primária
	@Id
	//Indique que o valor da chave primária deve ser gerado pelo próprio banco de dados de maneira sequencial
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(
		//Nome da coluna na base de dados
		name="ID_PRODUTO"
	)
	private Long idProduto;
	
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
		name = "PRECO", 
		//Tamanho do valor que será suportado
		precision = 9, 
		//Quantidade de casas decimais
		scale = 2,
		//Indica se a coluna pode possuir valores NULL
		nullable = false
	)
	private BigDecimal preco;

	public Produto() {
		super();
	}

	public Produto(Long idProduto, String nome, BigDecimal preco) {
		super();
		this.idProduto = idProduto;
		this.nome = nome;
		this.preco = preco;
	}

	public Long getIdProduto() {
		return idProduto;
	}

	public void setIdProduto(Long idProduto) {
		this.idProduto = idProduto;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}
	
	
	

}
