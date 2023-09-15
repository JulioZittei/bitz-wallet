package br.com.bitz.wallet.exception;

public class BalanceInsuficientException extends BusinessException{

    public BalanceInsuficientException (Object[] args) {
        super("Your current balance is {0}, but that costs {1}.", ErrorsCode.BTW002, args);

    }
}
