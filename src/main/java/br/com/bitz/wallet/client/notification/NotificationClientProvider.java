package br.com.bitz.wallet.client.notification;

import br.com.bitz.wallet.client.notification.response.NotificationDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationClientProvider {

    private final NotificationClient notificationClient;

    public NotificationDataResponse tryNotify() {
        return notificationClient.tryNotify();
    }

}
