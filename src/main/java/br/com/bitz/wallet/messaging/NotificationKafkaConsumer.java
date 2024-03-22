package br.com.bitz.wallet.messaging;

import br.com.bitz.wallet.client.notification.NotificationClientProvider;
import br.com.bitz.wallet.domain.model.response.TransactionDataResponse;
import br.com.bitz.wallet.exception.NotificationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationKafkaConsumer implements NotificationConsumer {

    private final NotificationClientProvider notificationClient;

    @KafkaListener(topics = "transaction-notification", groupId = "bitz-wallet")
    public void receiveNotification(TransactionDataResponse transaction) {
        log.info("Notifying transaction: %s".formatted(transaction.id()));

        var response = notificationClient.tryNotify();

        if (!response.message())
            throw new NotificationException();

        log.info("Notification has been sent: %b".formatted(response.message()));
    }
}
