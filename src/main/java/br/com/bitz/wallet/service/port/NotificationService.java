package br.com.bitz.wallet.service.port;

import br.com.bitz.wallet.domain.model.response.TransactionDataResponse;

public interface NotificationService {
    void execute(final TransactionDataResponse transaction);
}
