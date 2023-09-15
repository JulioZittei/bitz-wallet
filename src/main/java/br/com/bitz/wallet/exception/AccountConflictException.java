package br.com.bitz.wallet.exception;

public class AccountConflictException extends BusinessException {

    public AccountConflictException(Object[] args) {
        super("Already exists an account with {0} '{1}'.", ErrorsCode.BTW003, args);
    }
}