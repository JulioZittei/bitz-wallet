package br.com.bitz.wallet.controller.exception.handler;

import br.com.bitz.wallet.exception.ErrorsCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;

@ControllerAdvice
public class BitzWalletExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    private static final String TYPE = "type";
    private static final String CODE = "code";
    private static final String TITLE = "title";
    private static final String DETAIL = "detail";
    private static final String INSTANCE = "instance";
    private static final String INVALID_PARAMS = "invalid-params";


    public BitzWalletExceptionHandler(@Autowired MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, Object> problem = new LinkedHashMap<>();

        List<Map<String, Object>> invalidParams = new ArrayList<>();

        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            Map<String, Object> invalidParam = new HashMap<>();
            String message = messageSource.getMessage(error, LocaleContextHolder.getLocale());
            String field = ((FieldError) error).getField();
            invalidParam.put("field", field);
            invalidParam.put("message", message);
            invalidParams.add(invalidParam);
        }

        problem.put(CODE, ErrorsCode.BTW422.name());
        problem.put(TITLE, ErrorsCode.BTW422.getTitleText());
        problem.put(INSTANCE, ((ServletWebRequest) request).getRequest().getRequestURI());
        problem.put(INVALID_PARAMS, invalidParams);

        return handleExceptionInternal(ex, problem, headers, ErrorsCode.BTW422.getHttpStatusCode(), request);
    }

}
