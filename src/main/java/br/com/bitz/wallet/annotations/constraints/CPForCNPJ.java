package br.com.bitz.wallet.annotations.constraints;

import br.com.bitz.wallet.annotations.constraints.validators.CPForCNPJValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;


@Constraint(validatedBy = CPForCNPJValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CPForCNPJ {
    String message() default "{br.com.bitz.wallet.validator.constraints.CPForCNPJ.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
