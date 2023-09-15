package br.com.bitz.wallet.service.adapter;

import br.com.bitz.wallet.domain.entity.Account;
import br.com.bitz.wallet.domain.model.request.AccountRegisterDataRequest;
import br.com.bitz.wallet.domain.model.response.AccountDataResponse;
import br.com.bitz.wallet.exception.AccountConflictException;
import br.com.bitz.wallet.repository.account.AccountRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class CreateAccountServiceAdapterTest {

    @InjectMocks
    private CreateAccountServiceAdapter inTest;

    @Mock
    private AccountRepository accountRepository;


    @Test
    @DisplayName("should create an account with success")
     void shouldCreateAnAccountWithSuccess() {
        var requestData = new AccountRegisterDataRequest("João Da Silva", "00000000000", "joao@email.com", "senha", "PF");
        when(accountRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(accountRepository.findByDocument(anyString())).thenReturn(Optional.empty());
        when(accountRepository.save(any(Account.class))).then(invocation -> {
            var account = invocation.getArgument(0, Account.class);
            account.setId("id");
            return account;
        });

        AccountDataResponse createdAccount = inTest.execute(requestData);

        Assertions.assertThat(createdAccount).isNotNull();
        Assertions.assertThat(createdAccount.id()).isNotBlank();
        Assertions.assertThat(createdAccount.email()).isEqualTo(requestData.email());
        Assertions.assertThat(createdAccount.document()).isNotBlank();
        Assertions.assertThat(createdAccount.fullName()).isEqualTo(requestData.fullName());
        verify(accountRepository, times(1)).findByDocument(anyString());
        verify(accountRepository, times(1)).findByEmail(anyString());
        verify(accountRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("should not create an account and throws AccountConflictException when already exists an account with given document")
     void shouldNotCreateAnAccountAndThrowsAccountConflictExceptionWhenAlreadyExistsAnAccountWithGivenDocument() {
        var requestData = new AccountRegisterDataRequest("João Da Silva", "00000000000", "joao@email.com", "senha", "PF");
        when(accountRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(accountRepository.findByDocument(anyString())).thenReturn(Optional.of(mock(Account.class)));

        assertThrows(AccountConflictException.class, () -> inTest.execute(requestData));
    }

    @Test
    @DisplayName("should not create an account and throws AccountConflictException when already exists an account with given email")
     void shouldNotCreateAnAccountAndThrowsAccountConflictExceptionWhenAlreadyExistsAnAccountWithGivenEmail() {
        var requestData = new AccountRegisterDataRequest("João Da Silva", "00000000000", "joao@email.com", "senha", "PF");
        when(accountRepository.findByEmail(anyString())).thenReturn(Optional.of(mock(Account.class)));

        assertThrows(AccountConflictException.class, () -> inTest.execute(requestData));
    }
}
