package br.com.renanmatos.buypro.model;

import java.io.Serializable;
import java.util.Objects;

public class ProdutoVendedorId  implements Serializable {
	
	/*Os nomes dos atributos apontam diretamente para os nomes dos atributos da Entity, ProdutoVendedor no caso*/
	private Long vendedor;
	private Long produto;
	
	//Construtores
	public ProdutoVendedorId() {
	}

	public ProdutoVendedorId(Long vendedor, Long produto) {
		super();
		this.vendedor = vendedor;
		this.produto = produto;
	}
	//Equals and hashcode
	@Override
	public int hashCode() {
		return Objects.hash(produto, vendedor);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProdutoVendedorId other = (ProdutoVendedorId) obj;
		return Objects.equals(produto, other.produto) && Objects.equals(vendedor, other.vendedor);
	}

	// Getters
	public Long getVendedor() {
		return vendedor;
	}

	public void setVendedor(Long vendedor) {
		this.vendedor = vendedor;
	}

	public Long getProduto() {
		return produto;
	}

	public void setProduto(Long produto) {
		this.produto = produto;
	}
		
}
