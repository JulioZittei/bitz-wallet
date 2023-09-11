package br.com.bitz.wallet.service.adapter;

import br.com.bitz.wallet.client.OscorpClientProvider;
import br.com.bitz.wallet.client.response.OscorpDataResponse;
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

    private final OscorpClientProvider oscorpClientProvider;

    @Override
    public boolean authorize() {
        try {
            log.info("Requesting Oscorp external service");
            OscorpDataResponse response = oscorpClientProvider.authorize();
            log.info("Returning authorization response");
            return "Autorizado".equalsIgnoreCase(response.message());
        } catch(RetryableException ex) {
            log.error("Oscorp external service unavailable");
            throw new ServiceUnavailableException();
        }
    }
}
