package br.com.renanmatos.buypro.model;

import java.math.BigDecimal;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

//Entidade que representa o frete  
@Entity
@Table(name = "FRETE")
public class Frete {

	// Atributos da entidade
	@Id
	// Indique que o valor da chave primária deve ser gerado pelo próprio banco de
	// dados de maneira sequencial
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(
			// Nome da coluna na base de dados
			name = "ID_FRETE")
	private Long id;

//	@Column(
//			// Nome da coluna na base de dados
//			name = "PESO",
//			// Indica se a coluna pode possuir valores NULL
//			nullable = false)
//	private Double peso;

	@Column(
			// Nome da coluna na base de dados
			name = "VALOR_FRETE",
			// Indica se a coluna pode possuir valores NULL
			nullable = false)
	private BigDecimal valor;

	@OneToOne
	@JoinColumn(name = "ID_PEDIDO") // define a coluna que será a chave estrangeira no banco de dados
	private Pedido pedido; // referência ao pedido
	
	
	// Construtores, getters e setters

	public Frete() {
		super();
	}
	
	
	public Frete(Long id, Double peso, BigDecimal valor, Pedido pedido) {
		super();
		this.id = id;
//		this.peso = peso;
		this.valor = valor;
		this.pedido = pedido;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

//	public Double getPeso() {
//		return peso;
//	}

//	public void setPeso(Double peso) {
//		this.peso = peso;
//	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Frete other = (Frete) obj;
		return Objects.equals(id, other.id);
	}
	
	

}