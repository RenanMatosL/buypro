package br.com.renanmatos.buypro.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/*Anotação que habilita o Spring Boot no projeto*/
@SpringBootApplication(
	//Indicamos as funcionalidades padrão do Spring Boot que desejamos DESABILITAR
	exclude={
		/*Desabilitar a criação automática de datasource via leitura do arquivo application.properties (evita erro em cenário que não irá utilizar as propriedades padrão do Spring para 
		data source
		Deverá desabilitar esse item SOMENTE se NÃO for acessar base de dados em seu projeto OU se pretende criar MANUALMENTE o DataSource para conexão à base*/
		DataSourceAutoConfiguration.class
	}
)
//Indicar os pacotes que deverão ser mapeados pelo Spring (controllers, services, beans e etc)
//Em nosso exemplo, indicamos o PREFIXO dos nossos pacotes para que o Spring tente localizar itens dele em TODOS os pacotes do projeto
@ComponentScan(basePackages={"br.com.renanmatos.buypro"})
public class BuyproApplication {

	public static void main(String[] args) {
		SpringApplication.run(BuyproApplication.class, args);
	}

}
