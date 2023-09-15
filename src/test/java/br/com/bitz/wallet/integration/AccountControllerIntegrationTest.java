package br.com.bitz.wallet.integration;

import br.com.bitz.wallet.domain.model.request.AccountRegisterDataRequest;
import br.com.bitz.wallet.domain.model.request.AuthDataRequest;
import br.com.bitz.wallet.exception.ErrorsCode;
import br.com.bitz.wallet.repository.account.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

 class AccountControllerIntegrationTest extends ControllerIntegrationTest {

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    protected void beforeEach() throws Exception {
        var createRequest = new AccountRegisterDataRequest("João Da Silva",
                "00000000000000", "joao@email.com", "123456", "PF");
        createAccount(mapper.writeValueAsString(createRequest));
        var authRequest = new AuthDataRequest(createRequest.email(), createRequest.password());
        this.token = authenticateAccount(mapper.writeValueAsString(authRequest));
    }

    @Test
    @DisplayName("should get account logged information with success")
     void shouldGetAccountLoggedInformationWithSuccess() throws Exception {
        var headers = new HttpHeaders();
        headers.set(AUTHORIZATION, String.format(AUTH_FORMAT, token));

        get("/accounts/me", headers)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("João Da Silva"));
    }

    @Test
    @DisplayName("should not get account logged information when token is not informed")
     void shouldNotGetAccountLoggedInformationWhenTokenIsNotInformed() throws Exception {
        var headers = new HttpHeaders();
        headers.set(AUTHORIZATION, "");

        get("/accounts/me", headers)
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.title").value(ErrorsCode.BTW401.getTitleText()));
    }

    @Test
    @DisplayName("should not get account logged information when token is expired")
     void shouldNotGetAccountLoggedInformationWhenTokenIsExpired() throws Exception {
        var headers = new HttpHeaders();
        headers.set(AUTHORIZATION, "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJici5jb20uYml0eiIsImlhdCI6MTY5NDczMzMxMSwic3ViIjoianVsaW9AZW1haWwuY29tIiwiZXhwIjoxNjk0NzM2OTExfQ.WSKwmQvKgHqeewxz5NvT3NSPiuS2Et7EPnFIHj5J6Xw");

        get("/accounts/me", headers)
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.title").value(ErrorsCode.BTW401.getTitleText()));
    }

    @Test
    @DisplayName("should not get account logged information when account no longer exists")
     void shouldNotGetAccountLoggedInformationWhenAccountNoLongerExists() throws Exception {
        var headers = new HttpHeaders();
        headers.set(AUTHORIZATION, String.format(AUTH_FORMAT, token));

        accountRepository.deleteAll();

        get("/accounts/me", headers)
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.title").value(ErrorsCode.BTW401.getTitleText()));
    }
}
