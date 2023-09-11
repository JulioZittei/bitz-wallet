package br.com.bitz.wallet.exception;

import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ResourceBundle;

public class TransactionNotAuthorizedException extends BusinessException{
    public TransactionNotAuthorizedException () {
        super();
        this.code = ErrorsCode.BTW001;
    }

    @Override
    public String getMessage() {
        final ResourceBundle resourceBundle = ResourceBundle.getBundle(ErrorsCode.BUNDLE_NAME, LocaleContextHolder.getLocale());
        return resourceBundle.getString(this.code.name().concat(".detail"));
    }
}
