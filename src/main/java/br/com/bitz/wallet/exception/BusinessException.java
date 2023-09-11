package br.com.bitz.wallet.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BusinessException extends RuntimeException{
    protected ErrorsCode code;
    protected Map<String, Object> responseFields = new LinkedHashMap<>();

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException addField(String key, Object value) {
        responseFields.put(key, value);
        return this;
    }
}
