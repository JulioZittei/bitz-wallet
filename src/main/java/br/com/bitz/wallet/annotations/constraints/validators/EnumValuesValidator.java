package br.com.bitz.wallet.annotations.constraints.validators;

import br.com.bitz.wallet.annotations.constraints.EnumValues;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class EnumValuesValidator implements ConstraintValidator<EnumValues, String> {

    private Class<? extends Enum> enumClass;

    private final MessageSource messageSource;

    @Override
    public void initialize(final EnumValues constraintAnnotation) {
        this.enumClass = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        final Enum<?>[] values = this.enumClass.getEnumConstants();

        String messageTemplate = context.getDefaultConstraintMessageTemplate();
        String message = messageSource.getMessage(messageTemplate, new Object[]{}, LocaleContextHolder.getLocale());

        String acceptableValues = Arrays.stream(values).map(Enum::name).collect(Collectors.joining(","));

        message = message.replace("{acceptableValues}", acceptableValues);

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();

        return Objects.nonNull(values) && Arrays.stream(values).anyMatch(v -> v.name().equals(value));
    }
}
