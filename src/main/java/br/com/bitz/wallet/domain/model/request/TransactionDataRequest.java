package br.com.bitz.wallet.domain.model.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransactionDataRequest(

        @Positive
        @DecimalMax("50000.00")
        BigDecimal amount,

        @NotBlank
        String payer,

        @NotBlank
        String payee
) {
}
