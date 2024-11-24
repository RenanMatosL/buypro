package br.com.renanmatos.buypro.validacao;

import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotEmptyListValidator implements ConstraintValidator<NotEmptyList, List>{

	@Override
	public boolean isValid(List list, ConstraintValidatorContext constraintValidatorContext) {
		//Verifica se a lista está instanciada e preenchida
		return list != null && !list.isEmpty();
	}

	@Override
	public void initialize(NotEmptyList notEmptyList) {
		//Poderá invocar métodos da interface referente à anotação customizada 
		//notEmptyList.message();
	}
}
