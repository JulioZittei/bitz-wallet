package br.com.bitz.wallet.exception;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.ResourceBundle;

public enum ErrorsCode {

    BTW001(403),
    BTW002(422),
    BTW003(409),
    BTW004(404),
    BTW005(400),
    BTW006(400),
    BTW007(500),
    BTW400(400),
    BTW401(401),
    BTW403(403),
    BTW404(404),
    BTW405(405),
    BTW409(409),
    BTW410(410),
    BTW415(415),
    BTW422(422),
    BTW500(500),
    BTW501(501),
    BTW503(503);

    public static final String BUNDLE_NAME = "exceptions";
    public static final String TITLE = ".title";
    public static final String DETAIL = ".detail";
    public static final String STATUS = ".status";

    private Integer httpStatusCode;

    ErrorsCode(final Integer httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public String getTitleText() {
        final ResourceBundle resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, LocaleContextHolder.getLocale());
        String message = resourceBundle.getString(name().concat(TITLE));
        return new String(message.getBytes(), StandardCharsets.UTF_8);
    }

    public String getDetailText() {
        final ResourceBundle resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, LocaleContextHolder.getLocale());
        String message = resourceBundle.getString(name().concat(DETAIL));
        return new String(message.getBytes(), StandardCharsets.UTF_8);
    }

    public String getCodeText() {
        final ResourceBundle resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, LocaleContextHolder.getLocale());
        String message = resourceBundle.getString(name().concat(STATUS));
        return new String(message.getBytes(), StandardCharsets.UTF_8);
    }

    public Integer getCode() {
        return this.httpStatusCode;
    }

    public HttpStatus getHttpStatusCode() {
        return HttpStatus.valueOf(this.httpStatusCode);
    }


    public ErrorsCode valueOf(final Integer httpStatusCode) {

        if (Objects.isNull(httpStatusCode)) throw new NullPointerException("HttpStatusCode is null");

        for (ErrorsCode errorsCode : ErrorsCode.values()) {
            if (errorsCode.getCode().equals(httpStatusCode)) {
                return errorsCode;
            }
        }

        throw new IllegalArgumentException("No enum constant for code " + httpStatusCode);
    }

}
