package br.com.bitz.wallet.controller;

import br.com.bitz.wallet.domain.entity.Account;
import br.com.bitz.wallet.domain.entity.Transaction;
import br.com.bitz.wallet.domain.enums.AccountType;
import br.com.bitz.wallet.domain.model.request.TransactionDataRequest;
import br.com.bitz.wallet.domain.model.response.TransactionDataResponse;
import br.com.bitz.wallet.service.port.CreateTransactionService;
import br.com.bitz.wallet.service.port.FindTransactionsByPayerService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
 class TransactionControllerTest {

    @InjectMocks
    private TransactionController inTest;

    @Mock
    private CreateTransactionService createTransactionService;

    @Mock
    private FindTransactionsByPayerService findTransactionsByPayerService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    Authentication authentication;

    @Mock
    private UriComponentsBuilder uriComponentsBuilder;

    @Mock
    private UriComponents uriComponents;

    @Mock
    private URI uri;

    private static final Account account = Account.builder()
            .id("id")
            .accountType(AccountType.PF)
            .balance(new BigDecimal("20.00"))
            .email("joao@email.com")
            .fullName("Joao Da Silva")
            .document("00000000000000")
            .build();

    private static final Transaction transaction = Transaction.builder()
            .id("id")
            .payer(Account.builder().id("payer").build())
            .payee(Account.builder().id("payee").build())
            .amount(BigDecimal.valueOf(10))
            .createdAt(OffsetDateTime.now())
            .build();

    @Test
    @DisplayName("should create a transaction with success")
     void shouldCreateATransactionWithSuccess() {

        var request = new TransactionDataRequest(new BigDecimal(10), "payer", "payee");
        when(createTransactionService.execute(any())).thenReturn(new TransactionDataResponse(transaction));
        when(uriComponentsBuilder.path(anyString())).thenReturn(uriComponentsBuilder);
        when(uriComponentsBuilder.buildAndExpand(anyString())).thenReturn(uriComponents);
        when(uriComponents.toUri()).thenReturn(uri);

        ResponseEntity<TransactionDataResponse> responseEntity = inTest.createTransaction(request, uriComponentsBuilder);

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        Assertions.assertThat(responseEntity.getBody().id()).isEqualTo(transaction.getId());
        Assertions.assertThat(responseEntity.getBody().payer()).isEqualTo(transaction.getPayer().getId());
        Assertions.assertThat(responseEntity.getBody().payee()).isEqualTo(transaction.getPayee().getId());
    }

    @Test
    @DisplayName("should get transactions with success")
     void shouldGetTransactionsByPayerWithSuccess() {

        try (MockedStatic<SecurityContextHolder> holderMockedStatic = Mockito.mockStatic(SecurityContextHolder.class)) {

            var request = new TransactionDataRequest(new BigDecimal(10), "payer", "payee");
            when(findTransactionsByPayerService.execute(any())).thenReturn(List.of(new TransactionDataResponse(transaction)));
            holderMockedStatic.when( SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(account);

            ResponseEntity<List<TransactionDataResponse>> responseEntity = inTest.getTransactionsByPayer();

            Assertions.assertThat(responseEntity).isNotNull();
            Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
            Assertions.assertThat(responseEntity.getBody()).hasSize(1);
        }

    }
}
