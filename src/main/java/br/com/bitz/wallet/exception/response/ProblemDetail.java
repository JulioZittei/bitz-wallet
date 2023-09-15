package br.com.bitz.wallet.exception.response;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ProblemDetail {

    private ProblemDetail(){}

    public static ProblemDetailBuilder builder() {
        return new ProblemDetailBuilder();
    }

    public static class ProblemDetailBuilder {

        private Map<String, Object> problemDetail;
        public static final String TYPE = "type";
        public static final String CODE = "code";
        public static final String STATUS = "status";
        public static final String TITLE = "title";
        public static final String DETAIL = "detail";
        public static final String INSTANCE = "instance";
        public static final String INVALID_PARAMS = "invalid-params";

        ProblemDetailBuilder(){
            this.problemDetail = new LinkedHashMap<>();
        }

        public ProblemDetailBuilder type(String uri) {
            problemDetail.put(TYPE, uri);
            return this;
        }

        public ProblemDetailBuilder status(Integer code) {
            problemDetail.put(STATUS, code);
            return this;
        }

        public ProblemDetailBuilder code(String code) {
            problemDetail.put(CODE, code);
            return this;
        }


        public ProblemDetailBuilder title(String title) {
            problemDetail.put(TITLE, title);
            return this;
        }

        public ProblemDetailBuilder detail(String detail) {
            problemDetail.put(DETAIL, detail);
            return this;
        }

        public ProblemDetailBuilder instance(String uri) {
            problemDetail.put(INSTANCE, uri);
            return this;
        }

        public ProblemDetailBuilder invalidParams(List<Map<String, Object>> params) {
            problemDetail.put(INVALID_PARAMS, params);
            return this;
        }

        public ProblemDetailBuilder addProperty(String name, Object value) {
            problemDetail.put(name, value);
            return this;
        }

        public Map<String, Object> build() {
            return problemDetail;
        }
    }

}
