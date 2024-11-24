package br.com.renanmatos.buypro.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
//Indicar os pacotes que deverão ser mapeados pelo Spring (controllers, services, beans e etc)
//Em nosso exemplo, indicamos o PREFIXO dos nossos pacotes para que o Spring tente localizar itens dele em TODOS os pacotes do projeto
@ComponentScan(basePackages={"br.com.renanmatos.buypro.*"})
public class ConfiguracaoPadrao {

	//Bean responsável por possibilitar a utilização do: ${} no carregamento de propriedades de arquivos properties em beans do Spring
	@Bean
	public static PropertySourcesPlaceholderConfigurer getPropertySourcesPlaceholderConfigurer(){
		return new PropertySourcesPlaceholderConfigurer();
	}
}
