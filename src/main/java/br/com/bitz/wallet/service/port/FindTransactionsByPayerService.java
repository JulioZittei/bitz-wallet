package br.com.bitz.wallet.service.port;

import br.com.bitz.wallet.domain.model.response.TransactionDataResponse;

import java.util.List;

public interface FindTransactionsByPayerService {
    List<TransactionDataResponse> execute(String payerId);
}
