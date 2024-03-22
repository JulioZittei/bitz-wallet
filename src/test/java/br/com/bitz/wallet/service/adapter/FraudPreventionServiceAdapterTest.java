package br.com.bitz.wallet.service.adapter;

import br.com.bitz.wallet.client.authorizer.AuthorizerClientProvider;
import br.com.bitz.wallet.client.authorizer.response.AuthorizerDataResponse;
import br.com.bitz.wallet.exception.ServiceUnavailableException;
import feign.Request;
import feign.RetryableException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class FraudPreventionServiceAdapterTest {

    @InjectMocks
    private FraudPreventionServiceAdapter inTest;

    @Mock
    private AuthorizerClientProvider authorizerClientProvider;

    @Test
    @DisplayName("should authorize transaction with success")
     void shouldAuthorizeTransactionWithSuccess() {
        when(authorizerClientProvider.authorize()).thenReturn(new AuthorizerDataResponse("Autorizado"));

        boolean isAuthorized = inTest.authorize();
        
        Assertions.assertThat(isAuthorized).isTrue();
        verify(authorizerClientProvider, only()).authorize();
    }

    @Test
    @DisplayName("should not authorize transaction when provider responses not authorized")
     void shouldNotAuthorizeTransactionWhenProviderResponsesNotAuthorized() {
        when(authorizerClientProvider.authorize()).thenReturn(new AuthorizerDataResponse("Não Autorizado"));

        boolean isAuthorized = inTest.authorize();

        Assertions.assertThat(isAuthorized).isFalse();
        verify(authorizerClientProvider, only()).authorize();
    }

    @Test
    @DisplayName("should not authorize transaction and throw ServiceUnavailableException when provider is not available")
     void shouldNotAuthorizeTransactionAndThrowServiceUnavailableExceptionWhenProviderIsNotAvailable() {
        Request request = Request.create(Request.HttpMethod.GET,"/api/v1/transaction", new HashMap<>(), new byte[0], null);
        when(authorizerClientProvider.authorize()).thenThrow(new RetryableException(503, "Service unavailable", Request.HttpMethod.GET, new Date(), request));
        assertThrows(ServiceUnavailableException.class, ()-> inTest.authorize());
        verify(authorizerClientProvider, only()).authorize();
    }
}
