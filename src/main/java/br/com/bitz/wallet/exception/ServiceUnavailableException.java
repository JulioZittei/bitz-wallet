package br.com.bitz.wallet.exception;

public class ServiceUnavailableException extends BusinessException{

    public ServiceUnavailableException() {
        super("The service is temporarily unavailable.", ErrorsCode.BTW503, new Object[]{});
    }
}
