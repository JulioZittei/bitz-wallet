package br.com.bitz.wallet.service.adapter;

import br.com.bitz.wallet.domain.entity.Account;
import br.com.bitz.wallet.domain.entity.Transaction;
import br.com.bitz.wallet.domain.enums.AccountType;
import br.com.bitz.wallet.domain.model.request.TransactionDataRequest;
import br.com.bitz.wallet.domain.model.response.TransactionDataResponse;
import br.com.bitz.wallet.exception.BalanceInsuficientException;
import br.com.bitz.wallet.exception.PayeeNotFoundException;
import br.com.bitz.wallet.exception.PayerNotFoundException;
import br.com.bitz.wallet.exception.TransactionNotAuthorizedException;
import br.com.bitz.wallet.repository.account.AccountRepository;
import br.com.bitz.wallet.repository.transaction.TransactionRepository;
import br.com.bitz.wallet.service.port.FraudPreventionService;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class CreateTransactionServiceAdapterTest {

    @InjectMocks
    private CreateTransactionServiceAdapter inTest;

    @Mock
    private EntityManager entityManager;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private FraudPreventionService fraudPreventionService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private static final Account payer = Account.builder()
            .id("payer")
            .accountType(AccountType.PF)
            .balance(new BigDecimal("20.00"))
            .email("joao@email.com")
            .fullName("Joao Da Silva")
            .document("00000000000")
            .build();

    private static final Account payee = Account.builder()
            .id("payee")
            .accountType(AccountType.PF)
            .balance(new BigDecimal("20.00"))
            .email("maria@email.com")
            .fullName("Maria Da Silva")
            .document("00000000000")
            .build();

    @Test
    @DisplayName("should create a transaction with success")
     void shouldCreateATransactionWithSuccess() {

        try (MockedStatic<SecurityContextHolder> securityContexMockedStatic = mockStatic(SecurityContextHolder.class)) {

            var transactionRequest = new TransactionDataRequest(BigDecimal.valueOf(10), payer.getId(), payee.getId());

            when(accountRepository.findById(payer.getId())).thenReturn(Optional.of(payer));
            when(accountRepository.findById(payee.getId())).thenReturn(Optional.of(payee));
            when(fraudPreventionService.authorize()).thenReturn(true);
            when(transactionRepository.save(any(Transaction.class))).then(invocation -> {
                Transaction transaction = invocation.getArgument(0, Transaction.class);
                transaction.setId("transaction1");
                return transaction;
            });
            securityContexMockedStatic.when((MockedStatic.Verification) SecurityContextHolder.getContext()).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(payer);

            TransactionDataResponse transactionResponse = inTest.execute(transactionRequest);

            Assertions.assertThat(transactionResponse).isNotNull();
            Assertions.assertThat(transactionResponse.id()).isNotBlank();
            Assertions.assertThat(transactionResponse.amount()).isEqualTo(transactionRequest.amount());
            Assertions.assertThat(transactionResponse.payer()).isEqualTo(transactionRequest.payer());
            Assertions.assertThat(transactionResponse.payee()).isEqualTo(transactionRequest.payee());
            verify(accountRepository, times(2)).findById(anyString());
            verify(fraudPreventionService, only()).authorize();
            verify(transactionRepository, only()).save(any(Transaction.class));

        }
    }

    @Test
    @DisplayName("should not create a transaction and throw PayerNotFoundException")
     void shouldNotCreateATransactionAndThrowPayerNotFoundException() {
        var transactionRequest = new TransactionDataRequest(BigDecimal.valueOf(10), payer.getId(), payee.getId());
        assertThrows(PayerNotFoundException.class, () -> inTest.execute(transactionRequest));
    }

    @Test
    @DisplayName("should not create a transaction and throw PayeeNotFoundException")
     void shouldNotCreateATransactionAndThrowPayeeNotFoundException() {
        var transactionRequest = new TransactionDataRequest(BigDecimal.valueOf(10), payer.getId(), payee.getId());

        when(accountRepository.findById(payer.getId())).thenReturn(Optional.of(payer));

        assertThrows(PayeeNotFoundException.class, () -> inTest.execute(transactionRequest));
    }

    @Test
    @DisplayName("should not create a transaction and throws TransactionNotAuthorizedException")
     void shouldNotCreateATransactionAndThrowsTransactionNotAuthorizedException() {

        try (MockedStatic<SecurityContextHolder> securityContexMockedStatic = mockStatic(SecurityContextHolder.class)) {

            var transactionRequest = new TransactionDataRequest(BigDecimal.valueOf(10), payer.getId(), payee.getId());

            when(accountRepository.findById(payer.getId())).thenReturn(Optional.of(payer));
            when(accountRepository.findById(payee.getId())).thenReturn(Optional.of(payee));
            securityContexMockedStatic.when((MockedStatic.Verification) SecurityContextHolder.getContext()).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(payee);

            assertThrows(TransactionNotAuthorizedException.class, () -> inTest.execute(transactionRequest));
        }
    }

    @Test
    @DisplayName("should not create a transaction and throws BalanceInsuficientException")
     void shouldNotCreateATransactionAndThrowsBalanceInsuficientException() {

        try (MockedStatic<SecurityContextHolder> securityContexMockedStatic = mockStatic(SecurityContextHolder.class)) {

            var transactionRequest = new TransactionDataRequest(BigDecimal.valueOf(100), payer.getId(), payee.getId());

            when(accountRepository.findById(payer.getId())).thenReturn(Optional.of(payer));
            when(accountRepository.findById(payee.getId())).thenReturn(Optional.of(payee));
            securityContexMockedStatic.when((MockedStatic.Verification) SecurityContextHolder.getContext()).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(payer);

            assertThrows(BalanceInsuficientException.class, () -> inTest.execute(transactionRequest));
        }
    }

    @Test
    @DisplayName("should not create a transaction and throws TransactionNotAuthorizedException when fraud prevention service does not authorize transacation")
     void shouldNotCreateATransactionAndThrowsTransactionNotAuthorizedExceptionWhenFraudPreventionServiceDoesNotAuthorizeTransacation() {

        try (MockedStatic<SecurityContextHolder> securityContexMockedStatic = mockStatic(SecurityContextHolder.class)) {

            var transactionRequest = new TransactionDataRequest(BigDecimal.valueOf(10), payer.getId(), payee.getId());

            when(accountRepository.findById(payer.getId())).thenReturn(Optional.of(payer));
            when(accountRepository.findById(payee.getId())).thenReturn(Optional.of(payee));
            securityContexMockedStatic.when((MockedStatic.Verification) SecurityContextHolder.getContext()).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(payer);
            when(fraudPreventionService.authorize()).thenReturn(false);

            assertThrows(TransactionNotAuthorizedException.class, () -> inTest.execute(transactionRequest));
        }
    }
}
