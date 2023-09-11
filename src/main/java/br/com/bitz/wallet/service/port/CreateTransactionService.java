package br.com.bitz.wallet.service.port;

import br.com.bitz.wallet.domain.model.request.TransactionDataRequest;
import br.com.bitz.wallet.domain.model.response.TransactionDataResponse;

public interface CreateTransactionService {
    TransactionDataResponse execute(final TransactionDataRequest requestData);
}
