package br.com.bitz.wallet.service.adapter;

import br.com.bitz.wallet.client.authorizer.AuthorizerClientProvider;
import br.com.bitz.wallet.client.authorizer.response.AuthorizerDataResponse;
import br.com.bitz.wallet.exception.ServiceUnavailableException;
import br.com.bitz.wallet.service.port.FraudPreventionService;
import feign.RetryableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FraudPreventionServiceAdapter implements FraudPreventionService {

    private final AuthorizerClientProvider authorizerClientProvider;

    @Override
    public boolean authorize() {
        try {
            log.info("Requesting Authorizer service");
            AuthorizerDataResponse response = authorizerClientProvider.authorize();
            log.info("Returning authorization response");
            return "Autorizado".equalsIgnoreCase(response.message());
        } catch(RetryableException ex) {
            log.error("Authorizer service unavailable");
            throw new ServiceUnavailableException();
        }
    }
}
