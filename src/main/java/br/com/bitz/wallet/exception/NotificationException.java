package br.com.bitz.wallet.exception;

public class NotificationException extends BusinessException{
    public NotificationException () {
        super("Notification was not received", ErrorsCode.BTW007, new Object[]{});
    }
}
