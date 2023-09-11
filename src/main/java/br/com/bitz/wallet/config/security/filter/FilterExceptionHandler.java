package br.com.bitz.wallet.config.security.filter;

import br.com.bitz.wallet.config.log.StructuredLog;
import br.com.bitz.wallet.exception.ErrorsCode;
import br.com.bitz.wallet.exception.ErrorsField;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

@Slf4j
@Component
public class FilterExceptionHandler {

    private static final String MESSAGE = ".message";

    private static final String CLIENT_ERROR = "A client error occurred during the request";
    private static final String SERVER_ERROR = "A server error occurred during the request";

    public Map<String, Object> handleException(Exception ex, HttpServletRequest request) {
        if (ex instanceof JWTVerificationException jwtVerificationException) {
            return this.handleJWTVerification(jwtVerificationException, request);
        } else if (ex instanceof UsernameNotFoundException usernameNotFoundException) {
            return this.handleUsernameNotFound(usernameNotFoundException, request);
        }

        Map<String, Object> problem = new LinkedHashMap<>();

        StructuredLog.builder()
                .errorCode(ErrorsCode.BTW500.name())
                .errorMessage(ex.getMessage())
                .exception(ex.getClass());

        log.error(SERVER_ERROR);

        StructuredLog.remove()
                .errorCode()
                .errorMessage()
                .exception();

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
        String message = resourceBundle.getString(ex.getClass().getSimpleName().concat(MESSAGE));

        StructuredLog.builder()
                .errorCode(ErrorsCode.BTW401.name())
                .errorMessage(message)
                .exception(rootCause.getClass());

        log.error(CLIENT_ERROR);

        StructuredLog.remove()
                .errorCode()
                .errorMessage()
                .exception();

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

        StructuredLog.builder()
                .errorCode(ErrorsCode.BTW401.name())
                .errorMessage(message)
                .exception(ex.getClass());

        log.error(CLIENT_ERROR);

        StructuredLog.remove()
                .errorCode()
                .errorMessage()
                .exception();

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

        StructuredLog.builder()
                .errorCode(ErrorsCode.BTW401.name())
                .errorMessage(message)
                .exception(ex.getClass());

        log.error(CLIENT_ERROR);

        StructuredLog.remove()
                .errorCode()
                .errorMessage()
                .exception();

        problem.put(ErrorsField.CODE, ErrorsCode.BTW401.name());
        problem.put(ErrorsField.TITLE, ErrorsCode.BTW401.getTitleText());
        problem.put(ErrorsField.DETAIL, message);
        problem.put(ErrorsField.INSTANCE, request.getRequestURI());

        return problem;
    }
}
