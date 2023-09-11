package br.com.bitz.wallet.config.log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.regex.Pattern;

@Slf4j
@Component
@ControllerAdvice
public class LogInterceptor implements ResponseBodyAdvice<Object>, RequestBodyAdvice, HandlerInterceptor {

    private ObjectMapper mapper;

    private String method;
    private String path;
    private String response;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (mapper == null) {
            mapper = JsonMapper.builder()
                    .addModule(new JavaTimeModule())
                    .build();
        }
        return true;
    }

    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response,
                                final Object handler, final Exception ex) {
        StructuredLog.builder()
                .httpStatus(response.getStatus())
                .responseBody(this.response)
                .path(this.path)
                .method(this.method);
        log.info("Returning response");
        MDC.clear();
    }

    @Override
    public boolean supports(final MethodParameter returnType,
                            final Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(final Object obj,
                                  final MethodParameter returnType,
                                  final MediaType selectedContentType,
                                  final Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  final ServerHttpRequest request,
                                  final ServerHttpResponse response) {
        try {
            if (obj != null && !Pattern.matches(".*actuator.*", request.getURI().toString())) {
                this.response = mapper.writeValueAsString(obj);
                this.path = request.getURI().getPath();
                this.method = request.getMethod().name();
            }
        } catch (JsonProcessingException jpe) {
            log.error(jpe.getMessage(), jpe);

        }

        return obj;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        return inputMessage;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {

        try {
            if (body != null) {
                StructuredLog.builder()
                        .requestBody(mapper.writeValueAsString(body));
                log.info("Received request body");
                StructuredLog.remove().requestBody();
            }
        } catch (JsonProcessingException jpe) {
            log.error(jpe.getMessage(), jpe);

        }

        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }
}

