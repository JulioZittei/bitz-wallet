package br.com.bitz.wallet.client.notification;

import br.com.bitz.wallet.client.notification.response.NotificationDataResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "${spring.client.notification.name}",
    url = "${spring.client.notification.url}")
public interface NotificationClient {

    @GetMapping
    NotificationDataResponse tryNotify();
}
