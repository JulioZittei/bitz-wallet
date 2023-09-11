package br.com.bitz.wallet.service.adapter;

import br.com.bitz.wallet.domain.model.response.TransactionDataResponse;
import br.com.bitz.wallet.repository.transaction.TransactionRepository;
import br.com.bitz.wallet.repository.transaction.output.TransactionOutput;
import br.com.bitz.wallet.service.port.FindTransactionsByPayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FindTransactionsByPayerServiceAdapter implements FindTransactionsByPayerService {

    private final TransactionRepository transactionRepository;
    @Override
    public List<TransactionDataResponse> execute(String payerId) {
        List<TransactionOutput> transactions = transactionRepository.findTransactionsByPayer(payerId);
        return transactions.stream()
                .map(TransactionDataResponse::new)
                .toList();
    }
}
