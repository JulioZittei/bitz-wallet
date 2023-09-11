package br.com.bitz.wallet.service.port;

import br.com.bitz.wallet.domain.model.request.TransactionDataRequest;
import br.com.bitz.wallet.domain.model.response.TransactionDataResponse;

public interface TransactionService {
    TransactionDataResponse execute(final TransactionDataRequest requestData);
}
