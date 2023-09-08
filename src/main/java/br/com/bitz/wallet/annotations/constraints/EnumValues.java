package br.com.bitz.wallet.annotations.constraints;

import br.com.bitz.wallet.annotations.constraints.validators.EnumValuesValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = EnumValuesValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnumValues {
    String message() default "br.com.bitz.wallet.validator.constraints.Enum.message";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    Class<? extends Enum> value();
}
