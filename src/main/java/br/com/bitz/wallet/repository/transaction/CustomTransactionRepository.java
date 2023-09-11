package br.com.bitz.wallet.repository.transaction;

import br.com.bitz.wallet.repository.transaction.output.TransactionOutput;

import java.util.List;

public interface CustomTransactionRepository {

    List<TransactionOutput> findTransactionsByPayer(final String payerId);
}
