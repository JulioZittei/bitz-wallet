package br.com.bitz.wallet.annotations.constraints.validators;

import br.com.bitz.wallet.annotations.constraints.CPForCNPJ;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.hibernate.validator.internal.constraintvalidators.hv.br.CNPJValidator;
import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator;

public class CPForCNPJValidator implements ConstraintValidator<CPForCNPJ, String> {

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        CPFValidator cpfValidator = new CPFValidator();
        CNPJValidator cnpjValidator = new CNPJValidator();

        cpfValidator.initialize(null);
        cnpjValidator.initialize(null);

        return cpfValidator.isValid(value, context) || cnpjValidator.isValid(value, context);
    }
}
