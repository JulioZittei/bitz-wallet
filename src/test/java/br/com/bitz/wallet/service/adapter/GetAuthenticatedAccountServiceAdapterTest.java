package br.com.bitz.wallet.service.adapter;

import br.com.bitz.wallet.domain.entity.Account;
import br.com.bitz.wallet.domain.enums.AccountType;
import br.com.bitz.wallet.exception.AccountNotFoundException;
import br.com.bitz.wallet.repository.account.AccountRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class GetAuthenticatedAccountServiceAdapterTest {

    @InjectMocks
    private GetAuthenticatedAccountServiceAdapter inTest;

    @Mock
    private AccountRepository accountRepository;

    private static final String EMAIL = "joao@email.com";

    private static final Account authenticatedAccount = Account.builder()
            .id("id")
            .accountType(AccountType.PF)
            .balance(new BigDecimal("20.00"))
            .email("joao@email.com")
            .fullName("Joao Da Silva")
            .document("00000000000000")
            .build();


    @Test
    @DisplayName("should find an authenticated account with success")
     void shouldFindAnAuthenticatedAccountWithSuccess() {
        when(accountRepository.findByEmail(any())).thenReturn(Optional.of(this.authenticatedAccount));
        Account authenticatedAccount = inTest.execute(EMAIL);

        Assertions.assertThat(authenticatedAccount).isNotNull();
        Assertions.assertThat(authenticatedAccount.getEmail()).isEqualTo(EMAIL);
        verify(accountRepository, only()).findByEmail(any());
    }

    @Test
    @DisplayName("should not find an authenticated account and throws AccountNotFoundException")
     void shouldNotFindAnAuthenticatedAccountAndThrowsAccountNotFoundException() {
        assertThrows(AccountNotFoundException.class, ()-> inTest.execute(EMAIL));
        verify(accountRepository, only()).findByEmail(any());
    }
}
