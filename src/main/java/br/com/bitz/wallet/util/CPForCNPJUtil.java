package br.com.bitz.wallet.util;

import java.util.Objects;

public class CPForCNPJUtil {

    private CPForCNPJUtil(){}

    public static final Integer CPF_SIZE = 11;
    public static final Integer CNPJ_SIZE = 14;
    private static final String REGEX_CPF = "^[\\d]{3}([\\d]{6})[\\d]{2}$";
    private static final String REGEX_CNPJ = "^[\\d]{2}([\\d]{6})[\\d]{6}$";
    private static final String CPF_SEC_MASK = "***$1**";
    private static final String CNPJ_SEC_MASK = "**$1******";

    public static String maskSecurity(final String document) {
        Objects.requireNonNull(document, "document must not be null");

        if (CPF_SIZE.equals(document.length())) {
            return document.replaceAll(REGEX_CPF, CPF_SEC_MASK);
        } else if (CNPJ_SIZE.equals(document.length())) {
            return document.replaceAll(REGEX_CNPJ, CNPJ_SEC_MASK);
        }

        throw new IllegalArgumentException(String.format("document '%s' is not a CPF/CNPJ", document));
    }
}
