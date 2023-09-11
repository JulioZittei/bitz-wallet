package br.com.bitz.wallet.domain.model.response;

import br.com.bitz.wallet.domain.entity.Transaction;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record TransactionDataResponse(
        String id,
        BigDecimal amount,

        String payer,

        String payee,

        OffsetDateTime createdAt
) {
    public TransactionDataResponse(Transaction transaction) {
        this(transaction.getId(), transaction.getAmount(),
                transaction.getPayer().getId(),  transaction.getPayee().getId(), transaction.getCreatedAt());
    }
}
