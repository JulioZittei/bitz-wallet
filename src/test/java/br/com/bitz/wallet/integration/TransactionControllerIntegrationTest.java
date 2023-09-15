package br.com.bitz.wallet.integration;

import br.com.bitz.wallet.domain.model.request.AccountRegisterDataRequest;
import br.com.bitz.wallet.domain.model.request.AuthDataRequest;
import br.com.bitz.wallet.domain.model.request.TransactionDataRequest;
import br.com.bitz.wallet.domain.model.response.AccountDataResponse;
import br.com.bitz.wallet.exception.ErrorsCode;
import br.com.bitz.wallet.repository.account.AccountRepository;
import br.com.bitz.wallet.repository.transaction.TransactionRepository;
import br.com.bitz.wallet.service.port.FraudPreventionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

 class TransactionControllerIntegrationTest extends ControllerIntegrationTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @MockBean
    private FraudPreventionService fraudPreventionService;
    private AccountDataResponse payer;
    private AccountDataResponse payee;
    private String tokenPJ;

    @BeforeEach
    protected void setupTests() throws Exception {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();

        when(fraudPreventionService.authorize()).thenReturn(true);

        var payer = new AccountRegisterDataRequest("João Da Silva",
            "00000000000", "joao@email.com", "123456", "PF");
        var payee = new AccountRegisterDataRequest("Maria Da Silva",
            "00000000000000", "maria@email.com", "123456", "PJ");

        this.payer = createAccount(mapper.writeValueAsString(payer));
        this.payee = createAccount(mapper.writeValueAsString(payee));

        var authRequestPF = new AuthDataRequest(payer.email(), payer.password());
        var authRequestPJ = new AuthDataRequest(payee.email(), payee.password());
        this.token = authenticateAccount(mapper.writeValueAsString(authRequestPF));
        this.tokenPJ = authenticateAccount(mapper.writeValueAsString(authRequestPJ));
    }

    @Test
    @DisplayName("should create a transaction with success")
     void shouldCreateATransactionWithSuccess() throws Exception {
        var request = new TransactionDataRequest(BigDecimal.valueOf(10), payer.id(), payee.id());
        var headers = new HttpHeaders();
        headers.add(AUTHORIZATION, String.format(AUTH_FORMAT, token));

        post("/transactions", headers, mapper.writeValueAsString(request))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    @DisplayName("should not create a transaction when there is invalid fields")
     void shouldNotCreateATransactionWhenThereIsInvalidFields() throws Exception {
        var request = new TransactionDataRequest(BigDecimal.valueOf(10), "", payee.id());
        var headers = new HttpHeaders();
        headers.add(AUTHORIZATION, String.format(AUTH_FORMAT, token));

        post("/transactions", headers, mapper.writeValueAsString(request))
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.title").value(ErrorsCode.BTW422.getTitleText()));
    }

    @Test
    @DisplayName("should not create a transaction when token is not informed")
     void shouldNotCreateATransactionWhenTokenIsNotInformed() throws Exception {
        var request = new TransactionDataRequest(BigDecimal.valueOf(10), "", payee.id());
        var headers = new HttpHeaders();
        headers.add(AUTHORIZATION, String.format(AUTH_FORMAT, ""));

        post("/transactions", headers, mapper.writeValueAsString(request))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.title").value(ErrorsCode.BTW401.getTitleText()));
    }

    @Test
    @DisplayName("should not create a transaction when token is not valid")
     void shouldNotCreateATransactionWhenTokenIsNotValid() throws Exception {
        var request = new TransactionDataRequest(BigDecimal.valueOf(10), payer.id(), payee.id());
        var headers = new HttpHeaders();
        headers.add(AUTHORIZATION, String.format(AUTH_FORMAT, "token_invalid"));

        post("/transactions", headers, mapper.writeValueAsString(request))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.title").value(ErrorsCode.BTW401.getTitleText()));
    }

    @Test
    @DisplayName("should not create a transaction when token is expired")
     void shouldNotCreateATransactionWhenTokenIsExpired() throws Exception {
        var request = new TransactionDataRequest(BigDecimal.valueOf(10), "", payee.id());
        var headers = new HttpHeaders();
        headers.add(AUTHORIZATION, String.format(AUTH_FORMAT, "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJici5jb20uYml0eiIsImlhdCI6MTY5NDczMzMxMSwic3ViIjoianVsaW9AZW1haWwuY29tIiwiZXhwIjoxNjk0NzM2OTExfQ.WSKwmQvKgHqeewxz5NvT3NSPiuS2Et7EPnFIHj5J6Xw"));

        post("/transactions", headers, mapper.writeValueAsString(request))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.title").value(ErrorsCode.BTW401.getTitleText()));
    }

    @Test
    @DisplayName("should not create a transaction when account type is PJ")
     void shouldNotCreateATransactionWhenAccountYpeIsPJ() throws Exception {
        var request = new TransactionDataRequest(BigDecimal.valueOf(10), payer.id(), payee.id());
        var headers = new HttpHeaders();

        headers.add(AUTHORIZATION, String.format(AUTH_FORMAT, tokenPJ));

        post("/transactions", headers, mapper.writeValueAsString(request))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.title").value(ErrorsCode.BTW403.getTitleText()));
    }

    @Test
    @DisplayName("should not create a transaction when payer does not exists")
     void shouldNotCreateATransactionWhenPayerDoesNotExists() throws Exception {
        var request = new TransactionDataRequest(BigDecimal.valueOf(10), "does_no_exists", payee.id());
        var headers = new HttpHeaders();

        headers.add(AUTHORIZATION, String.format(AUTH_FORMAT, token));

        post("/transactions", headers, mapper.writeValueAsString(request))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.title").value(ErrorsCode.BTW006.getTitleText()))
            .andExpect(jsonPath("$.detail").value(ErrorsCode.BTW006.getDetailText()));
    }

    @Test
    @DisplayName("should not create a transaction when payee does not exists")
     void shouldNotCreateATransactionWhenPayeeDoesNotExists() throws Exception {
        var request = new TransactionDataRequest(BigDecimal.valueOf(10), payer.id(), "does_not_exists");
        var headers = new HttpHeaders();

        headers.add(AUTHORIZATION, String.format(AUTH_FORMAT, token));

        post("/transactions", headers, mapper.writeValueAsString(request))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.title").value(ErrorsCode.BTW005.getTitleText()))
            .andExpect(jsonPath("$.detail").value(ErrorsCode.BTW005.getDetailText()));
    }

    @Test
    @DisplayName("should not create a transaction when account logged is not owner of payer account")
     void shouldNotCreateATransactionWhenAccountLoggedIsNotOwnerOfPayerAccount() throws Exception {
        var request = new TransactionDataRequest(BigDecimal.valueOf(10), payee.id(), payer.id());
        var headers = new HttpHeaders();

        headers.add(AUTHORIZATION, String.format(AUTH_FORMAT, token));

        post("/transactions", headers, mapper.writeValueAsString(request))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.title").value(ErrorsCode.BTW001.getTitleText()))
            .andExpect(jsonPath("$.detail").value(ErrorsCode.BTW001.getDetailText()));
    }

    @Test
    @DisplayName("should not create a transaction when payer has insuficient balance")
     void shouldNotCreateATransactionWhenPayerHasInsuficientBalance() throws Exception {
        var request = new TransactionDataRequest(BigDecimal.valueOf(100), payer.id(), payee.id());
        var headers = new HttpHeaders();

        headers.add(AUTHORIZATION, String.format(AUTH_FORMAT, token));

        post("/transactions", headers, mapper.writeValueAsString(request))
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.title").value(ErrorsCode.BTW002.getTitleText()));
    }

    @Test
    @DisplayName("should not create a transaction when fraud prevention service does not authorize")
     void shouldNotCreateATransactionWhenFraudPreventionServiceDoesNotAuthorize() throws Exception {
        var request = new TransactionDataRequest(BigDecimal.valueOf(10), payer.id(), payee.id());
        var headers = new HttpHeaders();

        headers.add(AUTHORIZATION, String.format(AUTH_FORMAT, token));
        when(fraudPreventionService.authorize()).thenReturn(false);

        post("/transactions", headers, mapper.writeValueAsString(request))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.title").value(ErrorsCode.BTW001.getTitleText()))
            .andExpect(jsonPath("$.detail").value(ErrorsCode.BTW001.getDetailText()));
    }

    @Test
    @DisplayName("should get transactions by account logged with success")
     void shouldGetTransactionsByAccountLoggedWithSuccess() throws Exception {
        var headers = new HttpHeaders();
        headers.add(AUTHORIZATION, String.format(AUTH_FORMAT, token));

        get("/transactions", headers)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("should get transactions by account logged when token is not informed")
     void shouldNotGetTransactionsByAccountLoggedWhenTokenIsNotInformed() throws Exception {
        var headers = new HttpHeaders();
        headers.add(AUTHORIZATION, String.format(AUTH_FORMAT, ""));

        get("/transactions", headers)
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.title").value(ErrorsCode.BTW401.getTitleText()));
    }

    @Test
    @DisplayName("should get transactions by account logged when token is not valid")
     void shouldNotGetTransactionsByAccountLoggedWhenTokenIsNotValid() throws Exception {
        var headers = new HttpHeaders();
        headers.add(AUTHORIZATION, String.format(AUTH_FORMAT, "invalid_token"));

        get("/transactions", headers)
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.title").value(ErrorsCode.BTW401.getTitleText()));
    }

    @Test
    @DisplayName("should get transactions by account logged when token is expired")
     void shouldNotGetTransactionsByAccountLoggedWhenTokenIsExpired() throws Exception {
        var headers = new HttpHeaders();
        headers.add(AUTHORIZATION, String.format(AUTH_FORMAT, "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJici5jb20uYml0eiIsImlhdCI6MTY5NDczMzMxMSwic3ViIjoianVsaW9AZW1haWwuY29tIiwiZXhwIjoxNjk0NzM2OTExfQ.WSKwmQvKgHqeewxz5NvT3NSPiuS2Et7EPnFIHj5J6Xw"));

        get("/transactions", headers)
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.title").value(ErrorsCode.BTW401.getTitleText()));
    }
}
