package br.com.bitz.wallet.exception;

import org.springframework.context.i18n.LocaleContextHolder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ResourceBundle;

public class BalanceInsuficientException extends BusinessException{

    private final BigDecimal balance;

    private final BigDecimal amount;

    public BalanceInsuficientException (BigDecimal balance, BigDecimal amount) {
        super();
        this.code = ErrorsCode.BTW002;
        this.balance = balance;
        this.amount = amount;
    }

    @Override
    public String getMessage() {
        final ResourceBundle resourceBundle = ResourceBundle.getBundle(ErrorsCode.BUNDLE_NAME, LocaleContextHolder.getLocale());
        return resourceBundle.getString(this.code.name().concat(".detail"))
                .replace("{{balance}}", this.balance.setScale(2, RoundingMode.HALF_EVEN).toString())
                .replace("{{amount}}", this.amount.setScale(2, RoundingMode.HALF_EVEN).toString());
    }
}
