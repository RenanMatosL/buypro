package br.com.renanmatos.buypro.conf;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

@Configuration
//Habilitamos o controle automático de transações pelo modo PROXY (DEFAULT)
@EnableTransactionManagement(mode=AdviceMode.PROXY)
@EnableJpaRepositories(
	//Indicar a EntityManagerFactory (objeto JPA que manipula a base)
	entityManagerFactoryRef = "entityManagerFactory",
	//Indicar a TransactionManager (controlador de transações de banco de dados)
	transactionManagerRef = "transactionManager",
	/*Indicar o pacote que se encontram as classes DAO anotadas com @Repositoy que deverão utilizar o datasource gerado por essa classe*/
	basePackages = {
		"br.com.renanmatos.buypro.dao"
	}
)
//Indicar pacote que se encontram as classes anotadas com @Entity (entidades JPA)
@EntityScan(basePackages={"br.com.renanmatos.buypro.model"})
//Referencio o nome do arquivo properties a ser lido presente em “src/main/resources” (iremos carregar algumas de suas propriedades)
@PropertySource(value = "application.properties")
public class DataSourcePadrao {

	//Carregar algumas propriedades de arquivo properties indicado em @PropertySource
          @Value("${datasource.host}")
          private String hostBase;

          @Value("${datasource.usuario}")
          private String usuarioBase;

          @Value("${datasource.senha}")
          private String senhaBase;

          @Value("${datasource.hibernate.dialect}")
          private String dialetoBase;

          @Value("${datasource.driverClassName}")
          private String driverBase;

          @Value("${datasource.hibernate.showSql}")
          private String showSql;

	@Bean(name="jpaVendorAdapter") 
	public JpaVendorAdapter getJpaVendorAdapter () {
		//Criar a implementação do JPA a ser utilizada, Hibernate no caso
		HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
		return hibernateJpaVendorAdapter;
	}
	
	//Retorna um DataSource
	private DataSource getDataSource (){
		//Data Source exclusiva para servidor Tomcat
		BasicDataSource basicDataSource = new BasicDataSource();
		//Driver da base de dados
		basicDataSource.setDriverClassName(driverBase);
		//Host da base de dados
		basicDataSource.setUrl(hostBase);
		//Usuário da base de dados
		basicDataSource.setUsername(usuarioBase);
		//Senha da base de dados
		basicDataSource.setPassword(senhaBase);
		
	    return basicDataSource;
	}

	//Bean referente ao EntityManager do JPA
	@Bean(name = "entityManager")
	public EntityManager getEntityManager(){
    		return getEntityManagerFactory().createEntityManager();
	}

	//Bean referente a um EntityManagerFactory do JPA
	@Bean(name = "entityManagerFactory")
	public EntityManagerFactory getEntityManagerFactory(){
		//Indicar as propriedades do JPA desejadas
		Properties properties = new Properties();
		//Dialeto da base de dados
		properties.setProperty("hibernate.dialect", dialetoBase);
		//Indica se os comandos SQL deverão ser logados
		properties.put(
			"hibernate.show_sql", 
			showSql != null && showSql.equals(true) ? showSql : false
		);
		//Indica o modo que o Hibernate irá atualizar a base. Temos as opções: none, validate, update, #create, create-drop (Utilizar create apenas em ambiente local, com base de teste)
		properties.setProperty("hibernate.hbm2ddl.auto", "update");
		
	    	LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();

		//Indicar o DataSource
		localContainerEntityManagerFactoryBean.setDataSource(getDataSource());
		//Indicar a implementação do JPA a ser utilizada
		localContainerEntityManagerFactoryBean.setJpaVendorAdapter(getJpaVendorAdapter());
		
		/*Indicar o pacote que se encontram as classes anotadas com @Entity (entidades JPA)*/
		localContainerEntityManagerFactoryBean.setPackagesToScan(new String[]{
			"br.com.renanmatos.buypro.model"
		});
		
		localContainerEntityManagerFactoryBean.setPersistenceUnitName("persistenceUnit");

		//Indicar as propriedades do JPA
		localContainerEntityManagerFactoryBean.setJpaProperties(properties);
		
		localContainerEntityManagerFactoryBean.afterPropertiesSet();
	    
		return localContainerEntityManagerFactoryBean.getObject();
	}

	//Bean referente ao controle de transações de base de dados
	@Bean(name = "transactionManager")
	public PlatformTransactionManager transactionManager (){
		return new JpaTransactionManager(getEntityManagerFactory());
	}
}