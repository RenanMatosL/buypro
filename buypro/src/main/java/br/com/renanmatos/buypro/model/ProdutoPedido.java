package br.com.renanmatos.buypro.model;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
//Configurações da tabela
@Table(
	//Nome da tabela
	name="PRODUTO_PEDIDO"
)
//Indicamos a classe que representa as chaves estrangeiras do relacionamento ManyToMany
@IdClass(ProdutoPedidoId.class)
public class ProdutoPedido implements Serializable {

	//Atributos com anotações JPA
	//Indicamos que esse atributo representa compõe nossa chave composta
	@Id
	@ManyToOne
	//Indicamos o nome da chave estrangeira que deverá ser criado no banco de dados
	@JoinColumn(name = "ID_PEDIDO")
	private Pedido pedido;
	
	//Indicamos que esse atributo representa compõe nossa chave composta
	@Id
	@ManyToOne
	//Indicamos o nome da chave estrangeira que deverá ser criado no banco de dados
	@JoinColumn(name = "ID_PRODUTO")
	private Produto produto;

	@Column(
		//Nome da coluna na base de dados
		name = "QUANTIDADE", 
		//Tamanho do valor que será suportado
		precision = 9, 
		//Quantidade de casas decimais
		scale = 3,
		//Indica se a coluna pode possuir valores NULL
		nullable = false
	)
	private BigDecimal quantidade;

	@Column(
		//Nome da coluna na base de dados
		name = "VALOR_UNITARIO", 
		//Tamanho do valor que será suportado
		precision = 10, 
		//Quantidade de casas decimais
		scale = 2,
		//Indica se a coluna pode possuir valores NULL
		nullable = false
	)

	private BigDecimal valorUnitario;
	
	//Getters e setters


	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public BigDecimal getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	@Override
	public int hashCode() {
		return Objects.hash(pedido, produto);
	}

	//Crie métodos equals e hashcode com base nos atributos do relacionamento many to many (pedido e produto). SOLICITAR QUE O ECLIPSE GERE ESSES MÉTODOS!
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProdutoPedido other = (ProdutoPedido) obj;
		return Objects.equals(pedido, other.pedido) && Objects.equals(produto, other.produto);
	}
	
	
	
}
