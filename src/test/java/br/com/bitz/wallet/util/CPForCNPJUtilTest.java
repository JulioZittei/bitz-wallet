package br.com.bitz.wallet.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

 class CPForCNPJUtilTest {

    private static final String CPF = "12345678901";
    private static final String CNPJ = "12345678000120";

    @Test
    @DisplayName("should mask CPF to hide sensible information with success")
     void shouldMaskCPFToHideSensibleInformationWithSuccess() {
        String maskedDocument = CPForCNPJUtil.maskSecurity(CPF);

        Assertions.assertThat(maskedDocument)
            .isNotBlank()
            .contains("**");
    }

    @Test
    @DisplayName("should mask CNPJ to hide sensible information with success")
     void shouldMaskCNPJToHideSensibleInformationWithSuccess() {
        String maskedDocument = CPForCNPJUtil.maskSecurity(CNPJ);

        Assertions.assertThat(maskedDocument)
            .isNotBlank()
            .contains("**");
    }

    @Test
    @DisplayName("should not mask CPF/CNPJ and throw IllegalArgumentException")
     void shouldMaskCPFOrCNPJAndThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> CPForCNPJUtil.maskSecurity("invalid document"));
    }
}
