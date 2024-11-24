package br.com.renanmatos.buypro.conf;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
//Indicar os pacotes que deverão ser mapeados pelo Spring (controllers, services, beans e etc)
//Em nosso exemplo, indicamos o PREFIXO dos nossos pacotes para que o Spring tente localizar itens dele em TODOS os pacotes do projeto
@ComponentScan(basePackages={"br.com.renanmatos.buypro.*"})
public class MensagensConf {

	//Bean responsável por carregar em memória o arquivo properties de mensagens
	@Bean(name="MessageSourcePadrao")
	public MessageSource gerarMessageSource() {
		ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource = new ReloadableResourceBundleMessageSource();
		//Nome do arquivo properties (sem sua extensão) localizado no diretório "resources" (<diretório presente em resources>/<prefixo do arquivo>)
		reloadableResourceBundleMessageSource.setBasename("classpath:mensagens/mensagens");
		//Encode dos caracteres das mensagens
		reloadableResourceBundleMessageSource.setDefaultEncoding("ISO-8859-1");
		/*Indicamos o locale para que o properties da língua correta seja aplicado
		Poderá ter múltiplos properties, onde o padrão de nomenclatura deverá ter o MESMO prefixo indicado em "setBaseName" seguido pelo caractere - mais a sigla do locale
		*/
		reloadableResourceBundleMessageSource.setDefaultLocale(Locale.getDefault());
		return reloadableResourceBundleMessageSource;
	}
	
	//Bean responsável por trocar as CHAVES das propriedades referenciados em classes Java pelo VALOR dessas propriedades
	@Bean(name="localValidatorFactoryBeanPadrao")
	public LocalValidatorFactoryBean recuperarLocalValidatorFactoryBean() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		//Recuperar Bean com o arquivo properties de mensagens a ser aplicado
		localValidatorFactoryBean.setValidationMessageSource(gerarMessageSource());
		return localValidatorFactoryBean;
	}
}
