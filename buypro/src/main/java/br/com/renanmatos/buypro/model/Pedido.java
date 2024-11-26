package br.com.renanmatos.buypro.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import br.com.renanmatos.buypro.enuns.StatusPedido;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;

//Indica que se trata de entidade JPA
@Entity
//Configurações da tabela
@Table(
	//Nome da tabela
	name="PEDIDO"
)
public class Pedido implements Serializable{

	//Atributos com anotações JPA
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//Indica se tratar de chave primária
	@Id
	//Indique que o valor da chave primária deve ser gerado pelo próprio banco de dados de maneira sequencial
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(
		//Nome da coluna na base de dados
		name="ID_PEDIDO"
	)
	private Long idPedido;
	
	//Indica tipo de dados sendo DATA
	@Temporal(
		//Indica que o formato deve ser DATA e HORA
		TemporalType.TIMESTAMP
	)
	@Column(
		//Nome da coluna na base de dados
		name="PROTOCOLO_DATA_PEDIDO", 
		//Indica se a coluna pode possuir valores NULL
		nullable=false
	)
	private Date protocoloDataPedido;
	
	//indica ao JPA que o atributo não deve ser mapeado para uma coluna na tabela correspondente.
	@Transient
	private LocalDate DataPedido;

	//Indica que o valor a ser indicado na coluna deverá ser recuperado via Enum
	@Enumerated(
		//Indica que o valor NUMÉRICO do Enum deverá ser salvo na base de dados
		EnumType.ORDINAL
	)
	@Column(
		//Nome da coluna na base de dados
		name = "STATUS_PEDIDO", 
		//Indica se a coluna pode possuir valores NULL
		nullable=false
	)
	private StatusPedido statusPedidoAtivo;

	@Column(
		//Nome da coluna na base de dados
		name = "VALOR", 
		//Tamanho do valor que será suportado
		precision = 9, 
		//Quantidade de casas decimais
		scale = 2,
		//Indica se a coluna pode possuir valores NULL
		nullable = false
	)
	private BigDecimal valor;
	
	/*Relacionamento MUITOS para UM*/
	@ManyToOne(
		/*Indica que por PADRÃO, ao recuperar da base dados da entidade Pedido, o esse objeto relacionado DEVERÁ ser também recuperado (SERÁ realziado JOIN entre Pedido e Vendodor)*/
		fetch = FetchType.EAGER
	)
	//Anotação para configurar as colunas das tabelas envolvidas que aplicam o relacionamento
	@JoinColumn (
		//Nome da coluna (FK) que será criada na tabela dessa entidade (Pedido)
		name="ID_VENDEDOR", 
		//Nome da coluna PK da tabela da entidade relacionada (Vendedor)
		referencedColumnName="ID_VENDEDOR"
	)  
    private Vendedor vendedor; 

	/*Relacionamento MUITOS para UM*/
	@ManyToOne(
		/*Indica que por PADRÃO, ao recuperar da base dados da entidade Pedido, o esse objeto relacionado DEVERÁ ser também recuperado (SERÁ realziado JOIN entre Pedido e Cliente)*/
		fetch = FetchType.EAGER
	)
	//Anotação para configurar as colunas das tabelas envolvidas que aplicam o relacionamento
	@JoinColumn (
		//Nome da coluna (FK) que será criada na tabela dessa entidade (Pedido)
		name="ID_CLIENTE", 
		//Nome da coluna PK da tabela da entidade relacionada (Cliente)
		referencedColumnName="ID_CLIENTE"
	)
	private Cliente cliente;

	/*Relacionamento UM para MUITOS para implementação de relacionamento MANY to MANY com colunas EXTRAS!*/
	@OneToMany(
		//Obs. Ao indicar CascadeType.PERSIST ocorria erro: detached entity passed to persist: br.com.brunomatos.pedidos.model.Produto
		
		/*Atributo mappedBy é indicado nessa classe pois ela não é a dona da relação (não recebe a chave estrangeira)
		Indicamos o nome do atributo dessa classe (Pedido) presente na entidade relacionada (ProdutoPedido)*/
		mappedBy = "pedido",
		/*Indica que por PADRÃO, ao recuperar da base dados da entidade Pedido, essa lista relacionada DEVERÁ ser também recuperada (SERÁ realziado JOIN entre essas tabelas)*/
		fetch = FetchType.EAGER
	)
	/*Configuração para evitar erros caso essa entidade esteja se relacionando com MAIS de uma entidade em tipo de relacionamento FetchType.EAGER*/
	@Fetch(value = FetchMode.SUBSELECT)
	private List <ProdutoPedido> listaProdutoPedido = new ArrayList <> ();
	
	
	
	/*Relacionamento MUITOS para UM*/
	@ManyToOne(
		/*Indica que por PADRÃO, ao recuperar da base dados da entidade Pedido, o esse objeto relacionado DEVERÁ ser também recuperado (SERÁ realziado JOIN entre Pedido e frete)*/
		fetch = FetchType.EAGER
	)
	//Anotação para configurar as colunas das tabelas envolvidas que aplicam o relacionamento
	@JoinColumn (
		//Nome da coluna (FK) que será criada na tabela dessa entidade (Pedido)
		name="ID_FRETE", 
		//Nome da coluna PK da tabela da entidade relacionada (Frete)
		referencedColumnName="ID_FRETE"
	)

	private Frete frete;
	
	//Construtores
	public Pedido() {
	}
	
	public Pedido(Long idPedido, Date protocoloDataPedido, LocalDate dataPedido, StatusPedido statusPedidoAtivo,
			BigDecimal valor, Vendedor vendedor, Cliente cliente, List<ProdutoPedido> listaProdutoPedido, Frete frete) {
		super();
		this.idPedido = idPedido;
		this.protocoloDataPedido = protocoloDataPedido;
		DataPedido = dataPedido;
		this.statusPedidoAtivo = statusPedidoAtivo;
		this.valor = valor;
		this.vendedor = vendedor;
		this.cliente = cliente;
		this.listaProdutoPedido = listaProdutoPedido;
		this.frete = frete;
	}


	@Override
	public int hashCode() {
		return Objects.hash(idPedido);
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pedido other = (Pedido) obj;
		return Objects.equals(idPedido, other.idPedido);
	}

	public Long getIdPedido() {
		return idPedido;
	}

	public void setIdPedido(Long idPedido) {
		this.idPedido = idPedido;
	}

	public Date getProtocoloDataPedido() {
		return protocoloDataPedido;
	}

	public void setProtocoloDataPedido(Date protocoloDataPedido) {
		this.protocoloDataPedido = protocoloDataPedido;
	}

	public LocalDate getDataPedido() {
		return DataPedido;
	}

	public void setDataPedido(LocalDate dataPedido) {
		DataPedido = dataPedido;
	}

	public StatusPedido getStatusPedidoAtivo() {
		return statusPedidoAtivo;
	}

	public void setStatusPedidoAtivo(StatusPedido statusPedidoAtivo) {
		this.statusPedidoAtivo = statusPedidoAtivo;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Vendedor getVendedor() {
		return vendedor;
	}

	public void setVendedor(Vendedor vendedor) {
		this.vendedor = vendedor;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<ProdutoPedido> getListaProdutoPedido() {
		return listaProdutoPedido;
	}

	public void setListaProdutoPedido(List<ProdutoPedido> listaProdutoPedido) {
		this.listaProdutoPedido = listaProdutoPedido;
	}

	public Frete getFrete() {
		return frete;
	}

	public void setFrete(Frete frete) {
		this.frete = frete;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	


}
	








	


