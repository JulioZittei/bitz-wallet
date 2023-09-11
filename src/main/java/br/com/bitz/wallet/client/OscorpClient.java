package br.com.bitz.wallet.client;

import br.com.bitz.wallet.client.response.OscorpDataResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "${spring.client.fraud-prevention.name}",
        url = "${spring.client.fraud-prevention.url}")
public interface OscorpClient {

    @GetMapping
    OscorpDataResponse authorize();
}
