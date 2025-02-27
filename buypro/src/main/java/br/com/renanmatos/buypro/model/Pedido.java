package br.com.renanmatos.buypro.model;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import br.com.renanmatos.buypro.enuns.StatusPedido;
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
	name="PEDIDO"
)
public class Pedido implements Serializable{

	//Atributos com anotações JPA
	
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
		name="DATA_PEDIDO", 
		//Indica se a coluna pode possuir valores NULL
		nullable=false
	)
	private Date dataPedido;

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
	private StatusPedido statusPedido;

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
	private List <ProdutoPedido> listaProdutoPedido = new ArrayList();

	public Pedido() {
		super();
	}

	public Pedido(Long idPedido, Date dataPedido, StatusPedido statusPedido, BigDecimal valor, Cliente cliente,
			List<ProdutoPedido> listaProdutoPedido) {
		super();
		this.idPedido = idPedido;
		this.dataPedido = dataPedido;
		this.statusPedido = statusPedido;
		this.valor = valor;
		this.cliente = cliente;
		this.listaProdutoPedido = listaProdutoPedido;
	}

	public Long getIdPedido() {
		return idPedido;
	}

	public void setIdPedido(Long idPedido) {
		this.idPedido = idPedido;
	}

	public Date getDataPedido() {
		return dataPedido;
	}

	public void setDataPedido(Date dataPedido) {
		this.dataPedido = dataPedido;
	}

	public StatusPedido getStatusPedido() {
		return statusPedido;
	}

	public void setStatusPedido(StatusPedido statusPedido) {
		this.statusPedido = statusPedido;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
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

	/*Crie métodos equals e hashcode com base no atributo idPedido, pois o mesmo representa a chave primária dessa entidade. Ação necessária para o Java realizar corretamente 
	comparações entre objetos em embora seriam diferentes instâncias, se tratam do MESMO registro (pois possuem o mesmo ID identificador)*/

	
}

