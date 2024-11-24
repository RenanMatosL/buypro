package br.com.renanmatos.buypro.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

//Indica que se trata de entidade JPA
@Entity
//Configurações da tabela
@Table(
	//Nome da tabela
	name="PRODUTO"
)
public class Produto implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Atributos com anotações JPA  
    @Id  
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    @Column(name = "ID_PRODUTO")  
    private Long idProduto;  
    
    @Column (name = "CODIGO_PRODUTO", unique = true, nullable = false, length = 50)
    private String codigo;

    @Column(name = "NOME", length = 250, nullable = false)  
    private String nome;  

    @Column(name = "DESCRICAO", length = 20, nullable = false)  
    private String descricao;  
    
    @Column (name = "COR", length = 255, nullable = false )
    private String cor;
    
    @Column (name = "CATEGORIA", length = 255, nullable = false )
    private String categoria;
    
    @Column(name = "PRECO", precision = 9, scale = 2, nullable = false)
    	private BigDecimal preco;

    @Column(name = "PESO", precision = 9, scale = 2, nullable = false)  
    private BigDecimal peso; // Peso do produto   

    @Column(name = "ALTURA", length = 255, nullable = false)  
    private BigDecimal altura; // Altura do produto  

    @Column(name = "LARGURA", length = 255, nullable = false)  
    private BigDecimal largura; // Largura do produto  

    @Column(name = "COMPRIMENTO", length = 255, nullable = false)  
    private BigDecimal comprimento; // Comprimento do produto	
    

    //CONSTRUTORES
    
	public Produto() {
	}



	public Produto(Long idProduto, String codigo, String nome, String descricao, String cor, String categoria,
			BigDecimal preco, BigDecimal peso, BigDecimal altura, BigDecimal largura, BigDecimal comprimento) {
		super();
		this.idProduto = idProduto;
		this.codigo = codigo;
		this.nome = nome;
		this.descricao = descricao;
		this.cor = cor;
		this.categoria = categoria;
		this.preco = preco;
		this.peso = peso;
		this.altura = altura;
		this.largura = largura;
		this.comprimento = comprimento;
	}


	public Long getIdProduto() {
		return idProduto;
	}



	public void setIdProduto(Long idProduto) {
		this.idProduto = idProduto;
	}



	public String getCodigo() {
		return codigo;
	}



	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}



	public String getNome() {
		return nome;
	}



	public void setNome(String nome) {
		this.nome = nome;
	}



	public String getDescricao() {
		return descricao;
	}



	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}



	public String getCor() {
		return cor;
	}



	public void setCor(String cor) {
		this.cor = cor;
	}



	public String getCategoria() {
		return categoria;
	}



	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}



	public BigDecimal getPreco() {
		return preco;
	}



	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}



	public BigDecimal getPeso() {
		return peso;
	}



	public void setPeso(BigDecimal peso) {
		this.peso = peso;
	}



	public BigDecimal getAltura() {
		return altura;
	}



	public void setAltura(BigDecimal altura) {
		this.altura = altura;
	}



	public BigDecimal getLargura() {
		return largura;
	}



	public void setLargura(BigDecimal largura) {
		this.largura = largura;
	}



	public BigDecimal getComprimento() {
		return comprimento;
	}



	public void setComprimento(BigDecimal comprimento) {
		this.comprimento = comprimento;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Produto other = (Produto) obj;
		return Objects.equals(idProduto, other.idProduto);
	}

	  
    
}  