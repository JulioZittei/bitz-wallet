package br.com.bitz.wallet.client;

import br.com.bitz.wallet.client.response.OscorpDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OscorpClientProvider {

    private final OscorpClient client;

    public OscorpDataResponse authorize() {
        return client.authorize();
    }

}
