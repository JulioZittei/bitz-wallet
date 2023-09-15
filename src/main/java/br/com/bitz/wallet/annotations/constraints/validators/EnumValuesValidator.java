package br.com.bitz.wallet.annotations.constraints.validators;

import br.com.bitz.wallet.annotations.constraints.EnumValues;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;

import java.util.Arrays;
import java.util.Objects;

@RequiredArgsConstructor
public class EnumValuesValidator implements ConstraintValidator<EnumValues, String> {

    private Class<? extends Enum> enumClass;

    private String acceptableValues;

    private final MessageSource messageSource;

    @Override
    public void initialize(final EnumValues constraintAnnotation) {
        this.enumClass = constraintAnnotation.value();
        this.acceptableValues = constraintAnnotation.acceptableValues();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        final Enum<?>[] values = this.enumClass.getEnumConstants();

        return Objects.nonNull(values) && Arrays.stream(values).anyMatch(v -> v.name().equals(value));
    }
}
