package br.com.bitz.wallet.repository.transaction;

import br.com.bitz.wallet.domain.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID>, CustomTransactionRepository {

    List<Transaction> findByPayerId(String payerId);
}
