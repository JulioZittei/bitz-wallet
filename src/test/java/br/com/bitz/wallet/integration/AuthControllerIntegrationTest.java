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


 class AuthControllerIntegrationTest extends ControllerIntegrationTest{

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
     void clearDataBase() {
        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("should create an account with success")
     void shouldCreateAnAccountWithSuccess() throws Exception {

        var request = new AccountRegisterDataRequest("João Da Silva",
                "00000000000000", "joao@email.com", "123456", "PF");

        post("/auth/register", new HttpHeaders(), mapper.writeValueAsString(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fullName").value(request.fullName()));
    }

    @Test
    @DisplayName("should not create an account when there is invalid fields")
     void shouldNotCreateAnAccountWhenThereIsInvalidFields() throws Exception {

        var request = new AccountRegisterDataRequest("João Da Silva",
                "00000000000000", "joao@email.com", "12345", "PF");
        post("/auth/register", new HttpHeaders(), mapper.writeValueAsString(request))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.code").value(ErrorsCode.BTW422.name()));
    }

    @Test
    @DisplayName("should not create an account when request body is malformed")
     void shouldNotCreateAnAccountWhenRequestBodyIsMalformed() throws Exception {

        post("/auth/register", new HttpHeaders(), "{\"fullName\": \"João\" \"email\": \"joao@email.com\"}")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value(ErrorsCode.BTW400.getTitleText()));
    }

    @Test
    @DisplayName("should not create an account when already exists an account with given email")
     void shouldNotCreateAnAccountWhenAlreadyExistsAnAccountWithGivenEmail() throws Exception {

        var request = new AccountRegisterDataRequest("João Da Silva",
                "00000000000000", "joao@email.com", "123456", "PF");

        createAccount(mapper.writeValueAsString(request));

        post("/auth/register", new HttpHeaders(), mapper.writeValueAsString(request))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value(ErrorsCode.BTW409.getTitleText()));
    }

    @Test
    @DisplayName("should not create an account when already exists an account with given document")
     void shouldNotCreateAnAccountWhenAlreadyExistsAnAccountWithGivenDocument() throws Exception {

        var createRequest = new AccountRegisterDataRequest("João Da Silva",
                "00000000000000", "joao@email.com", "123456", "PF");

        var request = new AccountRegisterDataRequest("João Da Silva",
                "00000000000000", "joaoo@email.com", "123456", "PF");

        createAccount(mapper.writeValueAsString(createRequest));

        post("/auth/register", new HttpHeaders(), mapper.writeValueAsString(request))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value(ErrorsCode.BTW409.getTitleText()));
    }

    @Test
    @DisplayName("should authenticate an account with success")
     void shouldAuthenticateAnAccountWithSuccess() throws Exception {
        var createRequest = new AccountRegisterDataRequest("João Da Silva",
                "00000000000000", "joao@email.com", "123456", "PF");

        createAccount(mapper.writeValueAsString(createRequest));

        var request = new AuthDataRequest("joao@email.com", "123456");

        post("/auth/authenticate", new HttpHeaders(), mapper.writeValueAsString(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    @DisplayName("should not authenticate an account when there is invalid fields")
     void shouldNotAuthenticateAnAccountWhenThereIsInvalidFields() throws Exception {
        var createRequest = new AccountRegisterDataRequest("João Da Silva",
                "00000000000000", "joao@email.com", "123456", "PF");

        createAccount(mapper.writeValueAsString(createRequest));

        var request = new AuthDataRequest("joaoemail.com", "123456");

        post("/auth/authenticate", new HttpHeaders(), mapper.writeValueAsString(request))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.title").value(ErrorsCode.BTW422.getTitleText()));
    }

    @Test
    @DisplayName("should not authenticate an account when credentials is invalid")
     void shouldNotAuthenticateAnAccountWhenCredentialsIsInvalid() throws Exception {
        var createRequest = new AccountRegisterDataRequest("João Da Silva",
                "00000000000000", "joao@email.com", "123456", "PF");

        createAccount(mapper.writeValueAsString(createRequest));

        var request = new AuthDataRequest("joaoo@email.com", "123456");

        post("/auth/authenticate", new HttpHeaders(), mapper.writeValueAsString(request))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.title").value(ErrorsCode.BTW401.getTitleText()));
    }
}
