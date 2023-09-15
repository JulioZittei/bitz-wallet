package br.com.bitz.wallet.controller;

import br.com.bitz.wallet.domain.entity.Account;
import br.com.bitz.wallet.domain.enums.AccountType;
import br.com.bitz.wallet.domain.model.response.AccountDataResponse;
import br.com.bitz.wallet.service.port.GetAuthenticatedAccountService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
 class AccountControllerTest {

    @InjectMocks
    private AccountController inTest;

    @Mock
    private GetAuthenticatedAccountService getAuthenticatedAccountService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

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
    @DisplayName("should response an account data with success")
     void shouldResponseAnAccountDataWithSuccess() {

        try (MockedStatic<SecurityContextHolder> securityContexMockedStatic = mockStatic(SecurityContextHolder.class)) {

            when(getAuthenticatedAccountService.execute(anyString())).thenReturn(authenticatedAccount);
            securityContexMockedStatic.when((MockedStatic.Verification) SecurityContextHolder.getContext()).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(authenticatedAccount);

            ResponseEntity<AccountDataResponse> responseEntity = inTest.getAuthenticatedAccountData();

            Assertions.assertThat(responseEntity).isNotNull();
            Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
            Assertions.assertThat(responseEntity.getBody().email()).isEqualTo(EMAIL);
        }
    }
}
