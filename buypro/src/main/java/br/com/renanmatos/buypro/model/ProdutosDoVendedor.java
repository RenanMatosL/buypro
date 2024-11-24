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
@Table (name = "PRODUTO_DO_VENDEDOR")
@IdClass(ProdutoVendedorId.class)
public class ProdutosDoVendedor implements Serializable {
	//Atributos com anotações JPA
	//Indicamos que esse atributo representa compõe nossa chave composta
	@Id
	@ManyToOne
	//Indicamos o nome da chave estrangeira que deverá ser criado no banco de dados
	@JoinColumn(name = "ID_VENDEDOR")
	private Vendedor vendedor;
	
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
	
	
	

		@Override
		public int hashCode() {
			return Objects.hash(produto, vendedor);
		}

	
		//Equals and HashCode
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ProdutosDoVendedor other = (ProdutosDoVendedor) obj;
			return Objects.equals(produto, other.produto) && Objects.equals(vendedor, other.vendedor);
		}


		// Getters and Setters
		public Vendedor getVendedor() {
			return vendedor;
		}


		public void setVendedor(Vendedor vendedor) {
			this.vendedor = vendedor;
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

		

}
