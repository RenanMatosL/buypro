package br.com.renanmatos.buypro.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import br.com.renanmatos.buypro.enuns.StatusClienteAtivo;
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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

//Indica que se trata de entidade JPA
@Entity
//Configurações da tabela
@Table(
	//Nome da tabela
	name="CLIENTE"
)
public class Cliente implements Serializable{

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
		name="ID_CLIENTE"
	)
	private Long idCliente;
	
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
		name = "CPF", 
		//Quantidade máxima de caracteres da coluna
		length = 250,
 		//Indica se a coluna pode possuir valores NULL
		nullable = false,
		//Indica se a coluna é de valores ÚNICOS (não podendo haver mais de uma linha com o mesmo valor)
		unique=true
	)
	private String cpf;

	//Indica tipo de dados sendo DATA
	@Temporal(
		//Indica que o formato da data é SEM as horas
		TemporalType.DATE
	)
	@Column(
		//Nome da coluna na base de dados
		name="DATA_NASCIMENTO", 
		//Indica se a coluna pode possuir valores NULL
		nullable=false
	)
	private Date dataNascimento;

	//Indica tipo de dados sendo DATA
	@Temporal(
		//Indica que o formato deve ser DATA e HORA
		TemporalType.TIMESTAMP
	)
	@Column(
		//Nome da coluna na base de dados
		name="DATA_CADASTRO", 
		//Indica se a coluna pode possuir valores NULL
		nullable=false
	)
	private Date dataCadastro;

	//Indica que o valor a ser indicado na coluna deverá ser recuperado via Enum
	@Enumerated(
		//Indica que o valor NUMÉRICO do Enum deverá ser salvo na base de dados
		EnumType.ORDINAL
	)
	@Column(
		//Nome da coluna na base de dados
		name = "CLIENTE_ATIVO", 
		//Indica se a coluna pode possuir valores NULL
		nullable=false
	)
	private StatusClienteAtivo statusClienteAtivo;

	/*Relacionamento UM para MUITOS*/
    	@OneToMany(
		/*Indicamos os efeitos em cascatas que, se aplicados à entidade Cliente, deverão ser TAMBÉM aplicados essa lista de objetos relacionados*/
		cascade = {
			//Indicamos que se a entidade Cliente foi INSERIDA em base de dados, essa LISTA relacionada TAMBÉM deverá ser inserida 
			CascadeType.PERSIST
		}
		/*Atributo mappedBy é indicado nessa classe pois ela não é a dona da relação (não recebe a chave estrangeira)
		Indicamos o nome do atributo dessa classe presente na entidade relacionada*/
		,mappedBy = "cliente"
		/*Indica que por PADRÃO, ao recuperar da base dados da entidade Cliente, essa LISTA relacionada NÃO deverá ser recuperada (NÃO será realziado JOIN entre Cliente e Pedido)*/
		,fetch =  FetchType.LAZY
	)
    	/*Configuração para evitar erros caso essa entidade esteja se relacionando com MAIS de uma entidade em tipo de relacionamento FetchType.EAGER*/
    	@Fetch(value = FetchMode.SUBSELECT)
	private List<Pedido> listaPedidos = new ArrayList <>();

		
    //Construtores	
	public Cliente() {
	}

	public Cliente(Long idCliente, String nome, String cpf, Date dataNascimento, Date dataCadastro,
			StatusClienteAtivo statusClienteAtivo, List<Pedido> listaPedidos, List<Profissao> listaProfissoes) {
		super();
		this.idCliente = idCliente;
		this.nome = nome;
		this.cpf = cpf;
		this.dataNascimento = dataNascimento;
		this.dataCadastro = dataCadastro;
		this.statusClienteAtivo = statusClienteAtivo;
		this.listaPedidos = listaPedidos;
	}


	//Getters e Setters
	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
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

	public StatusClienteAtivo getStatusClienteAtivo() {
		return statusClienteAtivo;
	}

	public void setStatusClienteAtivo(StatusClienteAtivo statusClienteAtivo) {
		this.statusClienteAtivo = statusClienteAtivo;
	}

	public List<Pedido> getListaPedidos() {
		return listaPedidos;
	}

	public void setListaPedidos(List<Pedido> listaPedidos) {
		this.listaPedidos = listaPedidos;
	}

	@Override
	public int hashCode() {
		return Objects.hash(idCliente);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cliente other = (Cliente) obj;
		return Objects.equals(idCliente, other.idCliente);
	}


	
}