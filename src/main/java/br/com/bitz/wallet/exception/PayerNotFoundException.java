package br.com.bitz.wallet.exception;

public class PayerNotFoundException extends BusinessException {
    public PayerNotFoundException() {
        super("Payer not found.", ErrorsCode.BTW006, new Object[]{});
    }
}
