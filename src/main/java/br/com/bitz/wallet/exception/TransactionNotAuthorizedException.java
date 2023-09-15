package br.com.bitz.wallet.exception;

public class TransactionNotAuthorizedException extends BusinessException{
    public TransactionNotAuthorizedException () {
        super("Transaction not authorized", ErrorsCode.BTW001, new Object[]{});
    }
}
