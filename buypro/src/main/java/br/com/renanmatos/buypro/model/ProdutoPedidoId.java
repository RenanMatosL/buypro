package br.com.renanmatos.buypro.model;

import java.io.Serializable;
import java.util.Objects;

public class ProdutoPedidoId implements Serializable{

	/*Os nomes dos atributos apontam diretamente para os nomes dos atributos da Entity, ProdutoPedido no caso*/
	private Long pedido;
	private Long produto;

	public ProdutoPedidoId (){
	}

	public ProdutoPedidoId (Long pedido, Long produto){
		super();
		this. pedido =  pedido;
		this.produto =produto;
	}

	public Long getPedido() {
		return pedido;
	}

	public void setPedido(Long pedido) {
		this.pedido = pedido;
	}

	public Long getProduto() {
		return produto;
	}

	public void setProduto(Long produto) {
		this.produto = produto;
	}

	@Override
	public int hashCode() {
		return Objects.hash(pedido, produto);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProdutoPedidoId other = (ProdutoPedidoId) obj;
		return Objects.equals(pedido, other.pedido) && Objects.equals(produto, other.produto);
	}
	
	
	
 
}

