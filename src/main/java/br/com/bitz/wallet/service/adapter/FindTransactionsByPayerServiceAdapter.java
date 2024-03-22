package br.com.bitz.wallet.service.adapter;

import br.com.bitz.wallet.domain.entity.Transaction;
import br.com.bitz.wallet.domain.model.response.TransactionDataResponse;
import br.com.bitz.wallet.repository.transaction.TransactionRepository;
import br.com.bitz.wallet.service.port.FindTransactionsByPayerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FindTransactionsByPayerServiceAdapter implements FindTransactionsByPayerService {

    private final TransactionRepository transactionRepository;

    @Override
    public List<TransactionDataResponse> execute(String payerId) {
        log.info("Finding transactions by payer id {}", payerId);
        List<Transaction> transactions = transactionRepository.findByPayerId(payerId);
        log.info("Returning transactions");
        return transactions.stream()
                .map(TransactionDataResponse::new)
                .toList();
    }
}
