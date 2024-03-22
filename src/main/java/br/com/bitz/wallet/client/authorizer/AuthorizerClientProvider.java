package br.com.bitz.wallet.client.authorizer;

import br.com.bitz.wallet.client.authorizer.response.AuthorizerDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizerClientProvider {

    private final AuthorizerClient client;

    public AuthorizerDataResponse authorize() {
        return client.authorize();
    }

}
