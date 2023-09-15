package br.com.bitz.wallet.exception;

public class PayeeNotFoundException extends BusinessException {
    public PayeeNotFoundException() {
        super("Payee not found.", ErrorsCode.BTW005, new Object[]{});
    }
}
