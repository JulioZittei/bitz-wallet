package br.com.bitz.wallet.messaging;

import br.com.bitz.wallet.domain.model.response.TransactionDataResponse;

public interface NotificationConsumer {

    void receiveNotification(TransactionDataResponse transaction);
}
