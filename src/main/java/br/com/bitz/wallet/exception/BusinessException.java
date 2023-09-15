package br.com.bitz.wallet.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

@Getter
@AllArgsConstructor
public class BusinessException extends RuntimeException {

    protected final ErrorsCode code;
    protected final Object[] args;
    protected final Map<String, Object> responseFields;

    public BusinessException(final String message, final ErrorsCode code, final Object[] args) {
        super(message);
        this.responseFields =  new LinkedHashMap<>();
        this.code = code;
        this.args = args;
    }

    public BusinessException addField(String key, Object value) {
        responseFields.put(key, value);
        return this;
    }

    @Override
    public String getMessage() {
        return resolveMessage(super.getMessage(), args);
    }

    @Override
    public String getLocalizedMessage() {
        final ResourceBundle resourceBundle = ResourceBundle.getBundle(ErrorsCode.BUNDLE_NAME, LocaleContextHolder.getLocale());
        String message = resourceBundle.getString(this.code.name().concat(".detail"));
        return resolveMessage(message, args);

    }

    private String resolveMessage(final String message, final Object[] args) {
        String newMessage = message;

        for (int i = 0; i < args.length; i++) {
            newMessage = newMessage.replace(String.format("{%d}", i), args[i].toString());
        }

        return newMessage;
    }
}
