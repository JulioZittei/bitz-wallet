package br.com.bitz.wallet.exception;

public abstract class ErrorsField {

    private ErrorsField(){}
    public static final String TYPE = "type";
    public static final String CODE = "code";
    public static final String TITLE = "title";
    public static final String DETAIL = "detail";
    public static final String INSTANCE = "instance";
    public static final String INVALID_PARAMS = "invalid-params";
}
