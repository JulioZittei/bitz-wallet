package br.com.bitz.wallet.controller;

import br.com.bitz.wallet.domain.entity.Account;
import br.com.bitz.wallet.domain.enums.AccountType;
import br.com.bitz.wallet.domain.model.request.AccountRegisterDataRequest;
import br.com.bitz.wallet.domain.model.request.AuthDataRequest;
import br.com.bitz.wallet.domain.model.response.AccountDataResponse;
import br.com.bitz.wallet.domain.model.response.TokenDataResponse;
import br.com.bitz.wallet.service.port.AuthService;
import br.com.bitz.wallet.service.port.CreateAccountService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
 class AuthControllerTest {

    @InjectMocks
    private AuthController inTest;

    @Mock
    private CreateAccountService createAccountService;

    @Mock
    private AuthService authService;

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

    @Test
    @DisplayName("should resgister an account with success")
     void shouldResgisterAnAccountWithSuccess() {
        var request = new AccountRegisterDataRequest("joao@email.com",
                "00000000000000", "joao@email.com", "password", "PF");
        when(createAccountService.execute(any())).thenReturn(new AccountDataResponse(account));
        when(uriComponentsBuilder.path(anyString())).thenReturn(uriComponentsBuilder);
        when(uriComponentsBuilder.build()).thenReturn(uriComponents);
        when(uriComponents.toUri()).thenReturn(uri);
        ResponseEntity<AccountDataResponse> responseEntity = inTest.createAccount(request, uriComponentsBuilder);

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        Assertions.assertThat(responseEntity.getBody().email()).isEqualTo(request.email());
    }

    @Test
    @DisplayName("should authenticate an account with success")
     void shouldAuthenticateAnAccountWithSuccess() {
        var request = new AuthDataRequest("joao@email.com", "00000000000000");
        when(authService.execute(any())).thenReturn(new TokenDataResponse("valid_token"));
        ResponseEntity<TokenDataResponse> responseEntity = inTest.authenticateAccount(request);

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        Assertions.assertThat(responseEntity.getBody().token()).isNotBlank();
    }

}
