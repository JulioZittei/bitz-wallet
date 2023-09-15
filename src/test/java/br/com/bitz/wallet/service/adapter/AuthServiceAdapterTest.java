package br.com.bitz.wallet.service.adapter;

import br.com.bitz.wallet.domain.model.request.AuthDataRequest;
import br.com.bitz.wallet.domain.model.response.TokenDataResponse;
import br.com.bitz.wallet.repository.account.AccountRepository;
import br.com.bitz.wallet.service.port.JWTTokenService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class AuthServiceAdapterTest {

    @InjectMocks
    private AuthServiceAdapter inTest;

    @Mock
    private AuthenticationManager authManager;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private JWTTokenService jwtTokenService;

    @Mock
    private Authentication authentication;


    @Test
    @DisplayName("should authenticate an account with success")
     void shouldAuthenticateAnAccountWithSuccess() {

        AuthDataRequest authDataRequest = new AuthDataRequest("joao@email.com", "senha");
        when(authManager.authenticate(any())).thenReturn(authentication);
        when(jwtTokenService.generateToken(any())).thenReturn("Valid Token");

        TokenDataResponse tokenDataResponse = inTest.execute(authDataRequest);

        Assertions.assertThat(tokenDataResponse).isNotNull();
        Assertions.assertThat(tokenDataResponse.token()).isNotBlank();
        verify(authManager, times(1)).authenticate(any());
    }

    @Test
    @DisplayName("should not authenticate an account and throws BadCredentialsException")
     void shouldNotAuthenticateAnAccountAndThrowBadCredentialsException() {

        AuthDataRequest authDataRequest = new AuthDataRequest("joao@email.com", "senha");
        when(authManager.authenticate(any())).thenReturn(authentication);
        when(jwtTokenService.generateToken(any())).thenThrow(new InternalAuthenticationServiceException("Erro no login"));

        assertThrows(BadCredentialsException.class, ()-> inTest.execute(authDataRequest));
    }
}
