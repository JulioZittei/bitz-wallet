package br.com.bitz.wallet.service.adapter;

import br.com.bitz.wallet.domain.entity.Account;
import br.com.bitz.wallet.domain.enums.AccountType;
import br.com.bitz.wallet.repository.account.AccountRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import java.math.BigDecimal;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class AccountDetailsServiceTest {

    @InjectMocks
    private AccountDetailService inTest;

    @Mock
    AccountRepository accountRepository;

    private static final String EMAIL = "joao@email.com";

    private static final Account account = Account.builder()
            .id("id")
            .accountType(AccountType.PF)
            .balance(new BigDecimal("20.00"))
            .email("joao@email.com")
            .fullName("Joao Da Silva")
            .document("00000000000")
            .build();

    @Test
    @DisplayName("should load account by email")
     void shouldLoadAccountByEmail() {

        when(accountRepository.getByEmail(anyString())).thenReturn(account);
        UserDetails userDetails = inTest.loadUserByUsername(EMAIL);

        Assertions.assertThat(userDetails).isNotNull();
        Assertions.assertThat(userDetails.getUsername()).isEqualTo(EMAIL);
        verify(accountRepository, times(1)).getByEmail(anyString());

    }

    @Test
    @DisplayName("should not load account by email")
     void shouldNotLoadAccountByEmail() {

        UserDetails userDetails = inTest.loadUserByUsername(EMAIL);

        Assertions.assertThat(userDetails).isNull();
        verify(accountRepository, times(1)).getByEmail(anyString());

    }
}
