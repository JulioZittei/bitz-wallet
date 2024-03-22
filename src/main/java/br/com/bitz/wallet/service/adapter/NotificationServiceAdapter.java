package br.com.bitz.wallet.service.adapter;

import br.com.bitz.wallet.domain.model.response.TransactionDataResponse;
import br.com.bitz.wallet.messaging.NotificationProducer;
import br.com.bitz.wallet.service.port.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceAdapter  implements NotificationService {

    private final NotificationProducer notificationProducer;

    @Override
    public void execute(TransactionDataResponse transaction) {
        log.info("Notifying transaction: %s".formatted(transaction.id()));
        notificationProducer.sendNotification(transaction);
    }
}
