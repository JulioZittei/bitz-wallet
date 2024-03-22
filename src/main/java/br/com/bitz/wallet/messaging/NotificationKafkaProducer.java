package br.com.bitz.wallet.messaging;

import br.com.bitz.wallet.domain.model.response.TransactionDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationKafkaProducer implements NotificationProducer {

    private final KafkaTemplate<String, TransactionDataResponse> kafkaTemplate;
    @Override
    public void sendNotification(TransactionDataResponse transaction) {
        kafkaTemplate.send("transaction-notification", transaction);
    }
}
