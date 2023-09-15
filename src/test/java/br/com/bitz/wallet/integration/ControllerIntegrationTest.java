package br.com.bitz.wallet.integration;

import br.com.bitz.wallet.domain.model.request.TransactionDataRequest;
import br.com.bitz.wallet.domain.model.response.AccountDataResponse;
import br.com.bitz.wallet.domain.model.response.TokenDataResponse;
import br.com.bitz.wallet.domain.model.response.TransactionDataResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.Locale;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerIntegrationTest {

    @Autowired
    private MockMvc client;

    @LocalServerPort
    protected int port;

    @Autowired
    protected ObjectMapper mapper;

    protected String token = null;

    protected static final String AUTHORIZATION = "Authorization";
    protected static final String AUTH_FORMAT = "Bearer %s";


    @BeforeAll
    protected static void setup() throws Exception {
        LocaleContextHolder.setDefaultLocale(Locale.ENGLISH);
    }

    protected AccountDataResponse createAccount(String request) throws Exception {

        String responseAsString = post("/auth/register", new HttpHeaders(), request)
                .andReturn()
                .getResponse()
                .getContentAsString();
        return mapper.readValue(responseAsString, AccountDataResponse.class);
    }

    protected String authenticateAccount(String request) throws Exception {

        String responseAsString = post("/auth/authenticate", new HttpHeaders(), request)
            .andReturn()
            .getResponse()
            .getContentAsString();
        return mapper.readValue(responseAsString, TokenDataResponse.class).token();
    }

    protected TransactionDataResponse createTransaction(String request, HttpHeaders headers) throws Exception {
        String responseAsString = post("/transactions", headers, request)
            .andReturn()
            .getResponse()
            .getContentAsString();
        return mapper.readValue(responseAsString, TransactionDataResponse.class);
    }

    protected String createUrl(String uri) {
        return String.format("http://localhost:%d/api/v1%s", port, uri);
    }

    protected ResultActions post(String uri, HttpHeaders headers, String request) throws Exception {
        return client.perform(MockMvcRequestBuilders.post(createUrl(uri))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(request)
                .headers(headers));
    }

    protected ResultActions get(String uri, HttpHeaders headers) throws Exception {
        return client.perform(MockMvcRequestBuilders.get(createUrl(uri))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(headers));
    }
}
