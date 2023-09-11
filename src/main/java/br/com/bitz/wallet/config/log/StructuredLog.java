package br.com.bitz.wallet.config.log;

import feign.Response;
import org.slf4j.MDC;

public class StructuredLog {

    private StructuredLog() {
    }

    public static StructuredLogBuilder builder() {
        return new StructuredLogBuilder();
    }

    public static StructuredLogRemove remove() {
        return new StructuredLogRemove();
    }

    public static class StructuredLogBuilder {

        StructuredLogBuilder() {
        }

        public StructuredLogBuilder httpStatus(final int httpStatus) {
            MDC.put("httpStatus", String.valueOf(httpStatus));
            return this;
        }

        public StructuredLogBuilder path(final String path) {
            MDC.put("path", String.valueOf(path));
            return this;
        }

        public StructuredLogBuilder method(final String method) {
            MDC.put("method", String.valueOf(method));
            return this;
        }

        public StructuredLogBuilder requestBody(final String requestBody) {
            MDC.put("requestBody", requestBody);
            return this;
        }

        public StructuredLogBuilder responseBody(final String response) {
            MDC.put("responseBody", response);
            return this;
        }


        public StructuredLogBuilder feignResponseBody(final Response.Body feignResponseBody) {
            MDC.put("feignResponseBody", String.valueOf(feignResponseBody));
            return this;
        }

        public StructuredLogBuilder exception(final Class<? extends Throwable> ex) {
            MDC.put("exception", ex.getSimpleName());
            return this;
        }

        public StructuredLogBuilder errorCode(final String errorCode) {
            MDC.put("errorCode", errorCode);
            return this;
        }

        public StructuredLogBuilder externalErrorCode(final String externalErrorCode) {
            MDC.put("externalErrorCode", externalErrorCode);
            return this;
        }

        public StructuredLogBuilder errorMessage(final String errorMessage) {
            MDC.put("errorMessage", errorMessage);
            return this;
        }

        public StructuredLogBuilder externalErrorMessage(final String externalErrorMessage) {
            MDC.put("externalErrorMessage", externalErrorMessage);
            return this;
        }

    }

    public static class StructuredLogRemove {

        public StructuredLogRemove exception() {
            MDC.remove("exception");
            return this;
        }

        public StructuredLogRemove httpStatus() {
            MDC.remove("httpStatus");
            return this;
        }

        public StructuredLogRemove path() {
            MDC.remove("path");
            return this;
        }

        public StructuredLogRemove responseBody() {
            MDC.remove("responseBody");
            return this;
        }

        public StructuredLogRemove requestBody() {
            MDC.remove("requestBody");
            return this;
        }

        public StructuredLogRemove feignResponseBody() {
            MDC.remove("feignResponseBody");
            return this;
        }

        public StructuredLogRemove errorCode() {
            MDC.remove("errorCode");
            return this;
        }

        public StructuredLogRemove externalErrorCode() {
            MDC.remove("externalErrorCode");
            return this;
        }

        public StructuredLogRemove errorMessage() {
            MDC.remove("errorMessage");
            return this;
        }

        public StructuredLogRemove externalErrorMessage() {
            MDC.remove("externalErrorMessage");
            return this;
        }
    }
}
