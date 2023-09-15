package br.com.bitz.wallet.service.adapter;


import br.com.bitz.wallet.domain.entity.Account;
import br.com.bitz.wallet.domain.enums.AccountType;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
 class JWTTokenServiceAdapterTest {

    private JWTTokenServiceAdapter inTest = new JWTTokenServiceAdapter("secret_key", "br.com.bitz");

    private static final Account account = Account.builder()
            .accountType(AccountType.PF)
            .balance(new BigDecimal("20.00"))
            .email("joao@email.com")
            .fullName("Joao Da Silva")
            .document("00000000000")
            .build();

    @Test
    @DisplayName("should generate a token with success")
     void shouldGenerateATokenWithSuccess() {
        String token = inTest.generateToken(account);
        Assertions.assertThat(token).isNotBlank();
    }

    @Test
    @DisplayName("should not generate a token and throw JWTCreationException")
     void shouldNotGenerateATokenAndThrowJWTCreationException() {
        try (MockedStatic<JWT> jwtMockedStatic = Mockito.mockStatic(JWT.class)) {
            jwtMockedStatic.when(JWT::create).thenThrow(new JWTCreationException("Error creating token", new RuntimeException()));
            assertThrows(JWTCreationException.class, () -> inTest.generateToken(account));
        }
    }

    @Test
    @DisplayName("should verify a valid token with success")
     void shouldVerifyAValidTokenWithSuccess() {
        String token = inTest.generateToken(account);

        String email = inTest.isValid(token);
        Assertions.assertThat(email)
            .isNotBlank()
            .isEqualTo(account.getEmail());
    }

    @Test
    @DisplayName("should not valid a token and throw JWTVerificationException")
     void shouldNotGenerateATokenAndThrowJWTVerificationException() {
        assertThrows(JWTVerificationException.class, () -> inTest.isValid("invalid"));
    }
}
