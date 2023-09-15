package br.com.bitz.wallet.exception;

public class AccountNotFoundException extends BusinessException {

    public AccountNotFoundException() {
        super("Account not found.", ErrorsCode.BTW004, new Object[]{});
    }

}
