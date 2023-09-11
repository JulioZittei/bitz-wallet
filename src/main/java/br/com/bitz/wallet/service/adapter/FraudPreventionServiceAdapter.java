package br.com.bitz.wallet.service.adapter;

import br.com.bitz.wallet.client.OscorpClientProvider;
import br.com.bitz.wallet.client.response.OscorpDataResponse;
import br.com.bitz.wallet.service.port.FraudPreventionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FraudPreventionServiceAdapter implements FraudPreventionService {

    private final OscorpClientProvider oscorpClientProvider;

    @Override
    public boolean authorize() {
        OscorpDataResponse response = oscorpClientProvider.authorize();
        return "Autorizado".equalsIgnoreCase(response.message());
    }
}
