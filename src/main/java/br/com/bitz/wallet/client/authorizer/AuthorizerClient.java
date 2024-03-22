package br.com.bitz.wallet.client.authorizer;

import br.com.bitz.wallet.client.authorizer.response.AuthorizerDataResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "${spring.client.fraud-prevention.name}",
        url = "${spring.client.fraud-prevention.url}")
public interface AuthorizerClient {

    @GetMapping
    AuthorizerDataResponse authorize();
}
