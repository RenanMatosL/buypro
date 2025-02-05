package br.com.renanmatos.buypro.model;

import java.io.Serializable;
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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID_VENDEDOR")
	private Long idVendedor;
	
	@Column(
		name = "NOME", 
		length = 250,
		nullable = false
	)
	private String nome;
	
	@Column(
		name = "CPF", 
		length = 250,
		nullable = false,
		unique=true
	)
	private String cpf;

	@Temporal(TemporalType.DATE)
	@Column(
		name="DATA_NASCIMENTO", 
		nullable=false
	)
	private Date dataNascimento;

	@Temporal(
		TemporalType.TIMESTAMP
	)
	@Column(
		name="DATA_CADASTRO", 
		nullable=false
	)
	private Date dataCadastro;

	@Enumerated(
		EnumType.ORDINAL
	)
	@Column(
		name = "VENDEDOR_ATIVO", 
		nullable=false
	)
	private StatusVendedor statusVendedor;
	
	@OneToMany(
			//Obs. Ao indicar CascadeType.PERSIST ocorria erro: detached entity passed to persist: br.com.brunomatos.pedidos.model.Produto
			
			/*Atributo mappedBy é indicado nessa classe pois ela não é a dona da relação (não recebe a chave estrangeira)
			Indicamos o nome do atributo dessa classe (Pedido) presente na entidade relacionada (ProdutoPedido)*/
			mappedBy = "idPedido",
			/*Indica que por PADRÃO, ao recuperar da base dados da entidade Pedido, essa lista relacionada DEVERÁ ser também recuperada (SERÁ realziado JOIN entre essas tabelas)*/
			fetch = FetchType.EAGER
		)
		/*Configuração para evitar erros caso essa entidade esteja se relacionando com MAIS de uma entidade em tipo de relacionamento FetchType.EAGER*/
		@Fetch(value = FetchMode.SUBSELECT)
	private List<Pedido>pedidos;
	
	@OneToMany( fetch = FetchType.EAGER) @JoinColumn(name = "ID_VENDEDOR", referencedColumnName = "ID_VENDEDOR")
	private List<Produto>produtos;
	
	
	public Vendedor() {
		super();
	}
	
	
	public Vendedor(Long idVendedor, String nome, String cpf, Date dataNascimento, Date dataCadastro,
			StatusVendedor statusVendedor, List<Pedido> pedidos, List<Produto> produtos) {
		super();
		this.idVendedor = idVendedor;
		this.nome = nome;
		this.cpf = cpf;
		this.dataNascimento = dataNascimento;
		this.dataCadastro = dataCadastro;
		this.statusVendedor = statusVendedor;
		this.pedidos = pedidos;
		this.produtos = produtos;
	}



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


	public void setStatusVendedor(StatusVendedor statusVendedor) {
		this.statusVendedor = statusVendedor;
	}


	public List<Pedido> getPedidos() {
		return pedidos;
	}


	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}


	public List<Produto> getProdutos() {
		return produtos;
	}


	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
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
	
	
	

}