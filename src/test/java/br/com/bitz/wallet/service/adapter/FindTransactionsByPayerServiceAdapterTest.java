package br.com.bitz.wallet.service.adapter;

import br.com.bitz.wallet.domain.entity.Account;
import br.com.bitz.wallet.domain.entity.Transaction;
import br.com.bitz.wallet.domain.model.response.TransactionDataResponse;
import br.com.bitz.wallet.repository.transaction.TransactionRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class FindTransactionsByPayerServiceAdapterTest {

    @InjectMocks
    private FindTransactionsByPayerServiceAdapter inTest;

    @Mock
    private TransactionRepository transactionRepository;

    private static final String PAYER_ID = "payer_id";
    private static final String PAYEE_ID = "payee_id";

    private static final Transaction transaction = Transaction.builder()
            .id("id")
            .payer(Account.builder().id(PAYER_ID).build())
            .payee(Account.builder().id(PAYEE_ID).build())
            .amount(new BigDecimal("10.00"))
            .createdAt(OffsetDateTime.now())
            .build();

    @Test
    @DisplayName("should find transactions by payer with success")
     void shouldFindTransactionsByPayerWithSuccess() {
        when(transactionRepository.findByPayerId(PAYER_ID)).thenReturn(List.of(transaction));
        List<TransactionDataResponse> transactions = inTest.execute(PAYER_ID);

        Assertions.assertThat(transactions).isNotEmpty();
        Assertions.assertThat(transactions.get(0).id()).isEqualTo(transaction.getId());
        Assertions.assertThat(transactions.get(0).payer()).isEqualTo(transaction.getPayer().getId());
        Assertions.assertThat(transactions.get(0).payee()).isEqualTo(transaction.getPayee().getId());
        Assertions.assertThat(transactions.get(0).amount()).isEqualTo(transaction.getAmount());
        Assertions.assertThat(transactions.get(0).createdAt()).isEqualTo(transaction.getCreatedAt());
        verify(transactionRepository, only()).findByPayerId(anyString());
    }

    @Test
    @DisplayName("should not find transactions by payer")
     void shouldNotFindTransactionsByPayer() {
        when(transactionRepository.findByPayerId(PAYER_ID)).thenReturn(Collections.emptyList());
        List<TransactionDataResponse> transactions = inTest.execute(PAYER_ID);

        Assertions.assertThat(transactions).isEmpty();
        verify(transactionRepository, only()).findByPayerId(anyString());
    }
}
