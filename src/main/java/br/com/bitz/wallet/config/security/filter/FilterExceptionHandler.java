package br.com.bitz.wallet.config.security.filter;

import br.com.bitz.wallet.exception.ErrorsCode;
import br.com.bitz.wallet.exception.ErrorsField;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

@Component
public class FilterExceptionHandler {

    private static final String MESSAGE = ".message";
    public Map<String, Object> handleException(Exception ex, HttpServletRequest request) {
        if (ex instanceof JWTVerificationException jwtVerificationException) {
            return this.handleJWTVerification(jwtVerificationException, request);
        } else if (ex instanceof UsernameNotFoundException usernameNotFoundException) {
            return this.handleUsernameNotFound(usernameNotFoundException, request);
        }

        Map<String, Object> problem = new LinkedHashMap<>();

        problem.put(ErrorsField.CODE, ErrorsCode.BTW500.name());
        problem.put(ErrorsField.TITLE, ErrorsCode.BTW500.getTitleText());
        problem.put(ErrorsField.INSTANCE, request.getRequestURI());

        return problem;
    }

    protected Map<String, Object> handleJWTVerification(JWTVerificationException ex, HttpServletRequest request) {

        Throwable rootCause = ExceptionUtils.getRootCause(ex);

        if (rootCause instanceof TokenExpiredException tokenExpiredException) {
            return handleTokenExpired(tokenExpiredException, request);
        }

        Map<String, Object> problem = new LinkedHashMap<>();

        final ResourceBundle resourceBundle = ResourceBundle.getBundle(ErrorsCode.BUNDLE_NAME, LocaleContextHolder.getLocale());
        String message = resourceBundle.getString(((JWTVerificationException)ex).getClass().getSimpleName().concat(MESSAGE));

        problem.put(ErrorsField.CODE, ErrorsCode.BTW401.name());
        problem.put(ErrorsField.TITLE, ErrorsCode.BTW401.getTitleText());
        problem.put(ErrorsField.DETAIL, message);
        problem.put(ErrorsField.INSTANCE, request.getRequestURI());

        return problem;
    }

    protected Map<String, Object> handleTokenExpired(TokenExpiredException ex, HttpServletRequest request) {

        final ResourceBundle resourceBundle = ResourceBundle.getBundle(ErrorsCode.BUNDLE_NAME, LocaleContextHolder.getLocale());
        String message = resourceBundle.getString(ex.getClass().getSimpleName().concat(MESSAGE));

        Map<String, Object> problem = new LinkedHashMap<>();

        problem.put(ErrorsField.CODE, ErrorsCode.BTW401.name());
        problem.put(ErrorsField.TITLE, ErrorsCode.BTW401.getTitleText());
        problem.put(ErrorsField.DETAIL, message);
        problem.put(ErrorsField.INSTANCE, request.getRequestURI());

        return problem;
    }

    protected Map<String, Object> handleUsernameNotFound(UsernameNotFoundException ex, HttpServletRequest request) {

        final ResourceBundle resourceBundle = ResourceBundle.getBundle(ErrorsCode.BUNDLE_NAME, LocaleContextHolder.getLocale());
        String message = resourceBundle.getString(ex.getClass().getSimpleName().concat(MESSAGE));

        Map<String, Object> problem = new LinkedHashMap<>();

        problem.put(ErrorsField.CODE, ErrorsCode.BTW401.name());
        problem.put(ErrorsField.TITLE, ErrorsCode.BTW401.getTitleText());
        problem.put(ErrorsField.DETAIL, message);
        problem.put(ErrorsField.INSTANCE, request.getRequestURI());

        return problem;
    }
}
