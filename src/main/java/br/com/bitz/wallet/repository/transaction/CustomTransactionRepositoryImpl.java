package br.com.bitz.wallet.repository.transaction;

import br.com.bitz.wallet.repository.transaction.output.TransactionOutput;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomTransactionRepositoryImpl implements CustomTransactionRepository{

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<TransactionOutput> findTransactionsByPayer(String payerId) {
        StoredProcedureQuery storedProcedureQuery = entityManager.
                createStoredProcedureQuery("PC_FIND_TRANSACTIONS_BY_PAYER", TransactionOutput.class);

        storedProcedureQuery
                .registerStoredProcedureParameter("payerId", String.class, ParameterMode.IN);

        storedProcedureQuery.setParameter("payerId", payerId);

        storedProcedureQuery.execute();

        return storedProcedureQuery.getResultList();
    }
}
