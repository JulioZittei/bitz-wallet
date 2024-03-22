package br.com.bitz.wallet.controller.exception.handler;

import br.com.bitz.wallet.config.log.StructuredLog;
import br.com.bitz.wallet.exception.*;
import br.com.bitz.wallet.exception.response.ProblemDetail;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class BitzWalletExceptionHandler extends ResponseEntityExceptionHandler implements AccessDeniedHandler, AuthenticationEntryPoint {

    private final MessageSource messageSource;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String MESSAGE = ".message";
    private static final String CLIENT_ERROR = "A client error occurred during the request";
    private static final String SERVER_ERROR = "A server error occurred during the request";

    public BitzWalletExceptionHandler(@Autowired MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex) throws IOException {

        StructuredLog.builder()
                .errorCode(ErrorsCode.BTW403.name())
                .errorMessage(ErrorsCode.BTW403.getDetailText())
                .exception(ex.getClass());

        log.error(CLIENT_ERROR);

        StructuredLog.remove()
                .errorCode()
                .errorMessage()
                .exception();

        var problemDetail = ProblemDetail.builder()
                .status(ErrorsCode.BTW403.getCode())
                .code(ErrorsCode.BTW403.name())
                .title(ErrorsCode.BTW403.getTitleText())
                .detail(ErrorsCode.BTW403.getDetailText())
                .instance(request.getRequestURI() )
                .build();

        String responseBodyJson = objectMapper.writeValueAsString(problemDetail);

        response.setStatus(ErrorsCode.BTW403.getCode());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(responseBodyJson);
        response.getWriter().flush();
        response.getWriter().close();
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException {

        StructuredLog.builder()
                .errorCode(ErrorsCode.BTW401.name())
                .errorMessage(ErrorsCode.BTW401.getDetailText())
                .exception(ex.getClass());

        log.error(CLIENT_ERROR);

        StructuredLog.remove()
                .errorCode()
                .errorMessage()
                .exception();

        var problemDetail = ProblemDetail.builder()
                .status(ErrorsCode.BTW401.getCode())
                .code(ErrorsCode.BTW401.name())
                .title(ErrorsCode.BTW401.getTitleText())
                .detail(ErrorsCode.BTW401.getDetailText())
                .instance(request.getRequestURI())
                .build();

        String responseBodyJson = objectMapper.writeValueAsString(problemDetail);

        response.setStatus(ErrorsCode.BTW401.getCode());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(responseBodyJson);
        response.getWriter().flush();
        response.getWriter().close();
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<Object> handleServiceUnavailable(ServiceUnavailableException ex, WebRequest request) {

        StructuredLog.builder()
                .errorCode(ErrorsCode.BTW503.name())
                .errorMessage(ErrorsCode.BTW503.getDetailText())
                .exception(ex.getClass());

        log.error(SERVER_ERROR);

        StructuredLog.remove()
                .errorCode()
                .errorMessage()
                .exception();

        var problemDetail = ProblemDetail.builder()
                .status(ex.getCode().getCode())
                .code(ex.getCode().name())
                .title(ex.getCode().getTitleText())
                .detail(ex.getMessage())
                .instance(((ServletWebRequest) request).getRequest().getRequestURI())
                .build();

        return handleExceptionInternal(ex, problemDetail, new HttpHeaders(), ex.getCode().getHttpStatusCode(), request);
    }

    @ExceptionHandler(AccountConflictException.class)
    public ResponseEntity<Object> handleAccountConflict(AccountConflictException ex, WebRequest request) {

        StructuredLog.builder()
                .errorCode(ex.getCode().name())
                .errorMessage(ex.getLocalizedMessage())
                .exception(ex.getClass());

        log.error(CLIENT_ERROR);

        StructuredLog.remove()
                .errorCode()
                .errorMessage()
                .exception();

        var problemDetail = ProblemDetail.builder()
                .status(ex.getCode().getCode())
                .code(ex.getCode().name())
                .title(ex.getCode().getTitleText())
                .detail(ex.getLocalizedMessage())
                .instance(((ServletWebRequest) request).getRequest().getRequestURI())
                .build();

        if (!ex.getResponseFields().isEmpty()) {
            ex.getResponseFields().entrySet().stream().forEach((entry -> problemDetail.put(entry.getKey(), entry.getValue())));
        }

        return handleExceptionInternal(ex, problemDetail, new HttpHeaders(), ex.getCode().getHttpStatusCode(), request);
    }

    @ExceptionHandler(BalanceInsuficientException.class)
    public ResponseEntity<Object> handleBalanceInsuficient(BalanceInsuficientException ex, WebRequest request) {

        StructuredLog.builder()
                .errorCode(ex.getCode().name())
                .errorMessage(ex.getLocalizedMessage())
                .exception(ex.getClass());

        log.error(CLIENT_ERROR);

        StructuredLog.remove()
                .errorCode()
                .errorMessage()
                .exception();

        var problemDetail = ProblemDetail.builder()
                .status(ex.getCode().getCode())
                .code(ex.getCode().name())
                .title(ex.getCode().getTitleText())
                .detail(ex.getLocalizedMessage())
                .instance(((ServletWebRequest) request).getRequest().getRequestURI())
                .build();

        if (!ex.getResponseFields().isEmpty()) {
            ex.getResponseFields().entrySet().stream().forEach((entry -> problemDetail.put(entry.getKey(), entry.getValue())));
        }

        return handleExceptionInternal(ex, problemDetail, new HttpHeaders(), ex.getCode().getHttpStatusCode(), request);
    }

    @ExceptionHandler(TransactionNotAuthorizedException.class)
    public ResponseEntity<Object> handleTransactionNotAuthorized(TransactionNotAuthorizedException ex, WebRequest request) {

        StructuredLog.builder()
                .errorCode(ex.getCode().name())
                .errorMessage(ex.getLocalizedMessage())
                .exception(ex.getClass());

        log.error(CLIENT_ERROR);

        StructuredLog.remove()
                .errorCode()
                .errorMessage()
                .exception();

        var problemDetail = ProblemDetail.builder()
                .status(ex.getCode().getCode())
                .code(ex.getCode().name())
                .title(ex.getCode().getTitleText())
                .detail(ex.getLocalizedMessage())
                .instance(((ServletWebRequest) request).getRequest().getRequestURI())
                .build();

        if (!ex.getResponseFields().isEmpty()) {
            ex.getResponseFields().entrySet().stream().forEach((entry -> problemDetail.put(entry.getKey(), entry.getValue())));
        }

        return handleExceptionInternal(ex, problemDetail, new HttpHeaders(), ex.getCode().getHttpStatusCode(), request);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Object> handleAccountNotFound(AccountNotFoundException ex, WebRequest request) {

        StructuredLog.builder()
                .errorCode(ex.getCode().name())
                .errorMessage(ex.getLocalizedMessage())
                .exception(ex.getClass());

        log.error(CLIENT_ERROR);

        StructuredLog.remove()
                .errorCode()
                .errorMessage()
                .exception();

        var problemDetail = ProblemDetail.builder()
                .status(ex.getCode().getCode())
                .code(ex.getCode().name())
                .title(ex.getCode().getTitleText())
                .detail(ex.getLocalizedMessage())
                .instance(((ServletWebRequest) request).getRequest().getRequestURI())
                .build();

        if (!ex.getResponseFields().isEmpty()) {
            ex.getResponseFields().entrySet().stream().forEach((entry -> problemDetail.put(entry.getKey(), entry.getValue())));
        }

        return handleExceptionInternal(ex, problemDetail, new HttpHeaders(), ex.getCode().getHttpStatusCode(), request);
    }

    @ExceptionHandler(PayerNotFoundException.class)
    public ResponseEntity<Object> handlePayerNotFound(PayerNotFoundException ex, WebRequest request) {

        StructuredLog.builder()
                .errorCode(ex.getCode().name())
                .errorMessage(ex.getLocalizedMessage())
                .exception(ex.getClass());

        log.error(CLIENT_ERROR);

        StructuredLog.remove()
                .errorCode()
                .errorMessage()
                .exception();

        var problemDetail = ProblemDetail.builder()
                .status(ex.getCode().getCode())
                .code(ex.getCode().name())
                .title(ex.getCode().getTitleText())
                .detail(ex.getLocalizedMessage())
                .instance(((ServletWebRequest) request).getRequest().getRequestURI())
                .build();

        if (!ex.getResponseFields().isEmpty()) {
            ex.getResponseFields().entrySet().stream().forEach((entry -> problemDetail.put(entry.getKey(), entry.getValue())));
        }

        return handleExceptionInternal(ex, problemDetail, new HttpHeaders(), ex.getCode().getHttpStatusCode(), request);
    }

    @ExceptionHandler(PayeeNotFoundException.class)
    public ResponseEntity<Object> handlePayeeNotFound(PayeeNotFoundException ex, WebRequest request) {

        StructuredLog.builder()
                .errorCode(ex.getCode().name())
                .errorMessage(ex.getLocalizedMessage())
                .exception(ex.getClass());

        log.error(CLIENT_ERROR);

        StructuredLog.remove()
                .errorCode()
                .errorMessage()
                .exception();

        var problemDetail = ProblemDetail.builder()
                .status(ex.getCode().getCode())
                .code(ex.getCode().name())
                .title(ex.getCode().getTitleText())
                .detail(ex.getLocalizedMessage())
                .instance(((ServletWebRequest) request).getRequest().getRequestURI())
                .build();

        if (!ex.getResponseFields().isEmpty()) {
            ex.getResponseFields().entrySet().stream().forEach((entry -> problemDetail.put(entry.getKey(), entry.getValue())));
        }

        return handleExceptionInternal(ex, problemDetail, new HttpHeaders(), ex.getCode().getHttpStatusCode(), request);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentials(BadCredentialsException ex, WebRequest request) {

        final ResourceBundle resourceBundle = ResourceBundle.getBundle(ErrorsCode.BUNDLE_NAME, LocaleContextHolder.getLocale());
        String message = resourceBundle.getString(ex.getClass().getSimpleName().concat(MESSAGE));

        StructuredLog.builder()
                .errorCode(ErrorsCode.BTW401.name())
                .errorMessage(message)
                .exception(ex.getClass());

        log.error(CLIENT_ERROR);

        StructuredLog.remove()
                .errorCode()
                .errorMessage()
                .exception();

        var problemDetail = ProblemDetail.builder()
                .status(ErrorsCode.BTW401.getCode())
                .code(ErrorsCode.BTW401.name())
                .title(ErrorsCode.BTW401.getTitleText())
                .detail(message)
                .instance(((ServletWebRequest) request).getRequest().getRequestURI())
                .build();

        return handleExceptionInternal(ex, problemDetail, new HttpHeaders(), ErrorsCode.BTW401.getHttpStatusCode(), request);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        StructuredLog.builder()
                .errorCode(ErrorsCode.BTW404.name())
                .errorMessage(ErrorsCode.BTW404.getTitleText())
                .exception(ex.getClass());

        log.error(CLIENT_ERROR);

        StructuredLog.remove()
                .errorCode()
                .errorMessage()
                .exception();

        var problemDetail = ProblemDetail.builder()
                .status(ErrorsCode.BTW404.getCode())
                .code(ErrorsCode.BTW404.name())
                .title(ErrorsCode.BTW404.getTitleText())
                .instance(((ServletWebRequest) request).getRequest().getRequestURI())
                .build();

        return handleExceptionInternal(ex, problemDetail, new HttpHeaders(), ErrorsCode.BTW404.getHttpStatusCode(), request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        List<Map<String, Object>> invalidParams = new ArrayList<>();

        StructuredLog.builder()
                .errorCode(ErrorsCode.BTW422.name())
                .errorMessage(ErrorsCode.BTW422.getTitleText())
                .exception(ex.getClass());

        log.error(CLIENT_ERROR);

        StructuredLog.remove()
                .errorCode()
                .errorMessage()
                .exception();

        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            Map<String, Object> invalidParam = new HashMap<>();
            String message = messageSource.getMessage(error, LocaleContextHolder.getLocale());
            String field = ((FieldError) error).getField();
            invalidParam.put("name", field);
            invalidParam.put("message", message);
            invalidParams.add(invalidParam);
        }

        var problemDetail = ProblemDetail.builder()
                .status(ErrorsCode.BTW422.getCode())
                .code(ErrorsCode.BTW422.name())
                .title(ErrorsCode.BTW422.getTitleText())
                .instance(((ServletWebRequest) request).getRequest().getRequestURI())
                .invalidParams(invalidParams)
                .build();

        return handleExceptionInternal(ex, problemDetail, headers, ErrorsCode.BTW422.getHttpStatusCode(), request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        StructuredLog.builder()
                .errorCode(ErrorsCode.BTW415.name())
                .errorMessage(ErrorsCode.BTW415.getTitleText())
                .exception(ex.getClass());

        log.error(CLIENT_ERROR);

        StructuredLog.remove()
                .errorCode()
                .errorMessage()
                .exception();

        var problemDetail = ProblemDetail.builder()
                .status(ErrorsCode.BTW415.getCode())
                .code(ErrorsCode.BTW415.name())
                .title(ErrorsCode.BTW415.getTitleText())
                .instance(((ServletWebRequest) request).getRequest().getRequestURI())
                .build();

        return handleExceptionInternal(ex, problemDetail, headers, ErrorsCode.BTW415.getHttpStatusCode(), request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        StructuredLog.builder()
                .errorCode(ErrorsCode.BTW405.name())
                .errorMessage(ErrorsCode.BTW405.getTitleText())
                .exception(ex.getClass());

        log.error(CLIENT_ERROR);

        StructuredLog.remove()
                .errorCode()
                .errorMessage()
                .exception();

        var problemDetail = ProblemDetail.builder()
                .status(ErrorsCode.BTW405.getCode())
                .code(ErrorsCode.BTW405.name())
                .title(ErrorsCode.BTW405.getTitleText())
                .instance(((ServletWebRequest) request).getRequest().getRequestURI())
                .build();

        return handleExceptionInternal(ex, problemDetail, headers, ErrorsCode.BTW405.getHttpStatusCode(), request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Throwable rootCause = ExceptionUtils.getRootCause(ex);

        if (rootCause instanceof InvalidFormatException invalidFormatException) {
            return handleInvalidFormat(invalidFormatException, headers, request);

        } else if (rootCause instanceof JsonParseException jsonParseException) {
            return handleJsonProcessing(jsonParseException, headers, request);
        }

        StructuredLog.builder()
                .errorCode(ErrorsCode.BTW400.name())
                .errorMessage(rootCause.getMessage())
                .exception(rootCause.getClass());

        log.error(CLIENT_ERROR);

        StructuredLog.remove()
                .errorCode()
                .errorMessage()
                .exception();

        return handleExceptionInternal(ex, null, headers, ErrorsCode.BTW400.getHttpStatusCode(), request);
    }

    protected ResponseEntity<Object> handleInvalidFormat(InvalidFormatException ex, HttpHeaders headers, WebRequest request) {
        String field = ex.getPath().stream()
                .map(JsonMappingException.Reference::getFieldName)
                .collect(Collectors.joining("."));

        final ResourceBundle resourceBundle = ResourceBundle.getBundle(ErrorsCode.BUNDLE_NAME, LocaleContextHolder.getLocale());

        String message = resourceBundle.getString(ex.getClass().getSimpleName().concat(MESSAGE))
                .replace("{{field}}", field)
                .replace("{{value}}", String.valueOf(ex.getValue()))
                .replace("{{type}}", ex.getTargetType().getSimpleName());

        StructuredLog.builder()
                .errorCode(ErrorsCode.BTW400.name())
                .errorMessage(message)
                .exception(ex.getClass());

        log.error(CLIENT_ERROR);

        StructuredLog.remove()
                .errorCode()
                .errorMessage()
                .exception();

        var problemDetail = ProblemDetail.builder()
                .status(ErrorsCode.BTW400.getCode())
                .code(ErrorsCode.BTW400.name())
                .title(ErrorsCode.BTW400.getTitleText())
                .detail(message)
                .instance(((ServletWebRequest) request).getRequest().getRequestURI())
                .build();

        return handleExceptionInternal(ex, problemDetail, headers, ErrorsCode.BTW400.getHttpStatusCode(), request);
    }

    protected ResponseEntity<Object> handleJsonProcessing(JsonProcessingException ex, HttpHeaders headers, WebRequest request) {

        final ResourceBundle resourceBundle = ResourceBundle.getBundle(ErrorsCode.BUNDLE_NAME, LocaleContextHolder.getLocale());

        String message = resourceBundle.getString(ex.getClass().getSimpleName().concat(MESSAGE));

        StructuredLog.builder()
                .errorCode(ErrorsCode.BTW400.name())
                .errorMessage(message)
                .exception(ex.getClass());

        log.error(CLIENT_ERROR);

        StructuredLog.remove()
                .errorCode()
                .errorMessage()
                .exception();

        var problemDetail = ProblemDetail.builder()
                .status(ErrorsCode.BTW400.getCode())
                .code(ErrorsCode.BTW400.name())
                .title(ErrorsCode.BTW400.getTitleText())
                .detail(message)
                .instance(((ServletWebRequest) request).getRequest().getRequestURI())
                .build();

        return handleExceptionInternal(ex, problemDetail, headers, ErrorsCode.BTW400.getHttpStatusCode(), request);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<Object> handleMissingRequestHeaderException(MissingRequestHeaderException ex, WebRequest request) {
        final ResourceBundle resourceBundle = ResourceBundle.getBundle(ErrorsCode.BUNDLE_NAME, LocaleContextHolder.getLocale());
        String message = resourceBundle.getString(ex.getClass().getSimpleName().concat(MESSAGE))
            .replace("{{field}}", ex.getHeaderName());

        StructuredLog.builder()
            .errorCode(ErrorsCode.BTW400.name())
            .errorMessage(message)
            .exception(ex.getClass());

        log.error(CLIENT_ERROR);

        StructuredLog.remove()
            .errorCode()
            .errorMessage()
            .exception();

        var problemDetail = ProblemDetail.builder()
            .status(ErrorsCode.BTW400.getCode())
            .code(ErrorsCode.BTW400.name())
            .title(ErrorsCode.BTW400.getTitleText())
            .detail(message)
            .instance(((ServletWebRequest) request).getRequest().getRequestURI())
            .build();

        return handleExceptionInternal(ex, problemDetail, new HttpHeaders(), ErrorsCode.BTW400.getHttpStatusCode(), request);

    }
}

