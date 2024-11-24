package br.com.renanmatos.buypro.validacao;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

//Indico que a ação deve ser aplicada em tempo de execução apenas
@Retention(RetentionPolicy.RUNTIME)
//Indico que essa anotação customizada poderá ser aplicada apenas em atributos
@Target(ElementType.FIELD)
//Indico a classe validadora dessa anotação
@Constraint(validatedBy=NotEmptyListValidator.class)
public @interface NotEmptyList {
	String message() default "A lista não pode ser vazia";
	
	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}
