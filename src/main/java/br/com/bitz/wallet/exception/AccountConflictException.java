package br.com.bitz.wallet.exception;

import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ResourceBundle;

public class AccountConflictException extends BusinessException {

    private final String field;

    private final String value;

    public AccountConflictException(String field, String value) {
        super();
        this.field = field;
        this.value = value;
        this.code = ErrorsCode.BTW003;
    }

    @Override
    public String getMessage() {
        final ResourceBundle resourceBundle = ResourceBundle.getBundle(ErrorsCode.BUNDLE_NAME, LocaleContextHolder.getLocale());
        return resourceBundle.getString(this.code.name().concat(".detail"))
                .replace("{{field}}", this.field)
                .replace("{{value}}", this.value);
    }
}
