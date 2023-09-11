package br.com.bitz.wallet.service.adapter;

import br.com.bitz.wallet.domain.entity.Account;
import br.com.bitz.wallet.domain.entity.Transaction;
import br.com.bitz.wallet.domain.model.request.TransactionDataRequest;
import br.com.bitz.wallet.domain.model.response.TransactionDataResponse;
import br.com.bitz.wallet.exception.BalanceInsuficientException;
import br.com.bitz.wallet.exception.PayeeNotFoundException;
import br.com.bitz.wallet.exception.PayerNotFoundException;
import br.com.bitz.wallet.exception.TransactionNotAuthorizedException;
import br.com.bitz.wallet.repository.account.AccountRepository;
import br.com.bitz.wallet.repository.transaction.TransactionRepository;
import br.com.bitz.wallet.service.port.FraudPreventionService;
import br.com.bitz.wallet.service.port.CreateTransactionService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class CreateTransactionServiceAdapter implements CreateTransactionService {

    private final EntityManager entityManager;

    private final TransactionRepository transactionRepository;

    private final AccountRepository accountRepository;

    private final FraudPreventionService fraudPreventionService;

    private static final String PAYER = "payer";
    private static final String PAYEE = "payee";
    private static final String AMOUNT = "amount";
    private static final String BALANCE = "balance";

    @Override
    @Transactional
    public TransactionDataResponse execute(final TransactionDataRequest requestData) {
        Account payer = accountRepository.findById(requestData.payer())
                .orElseThrow(() -> new PayerNotFoundException()
                        .addField(PAYER, requestData.payer()));
        Account payee = accountRepository.findById(requestData.payee())
                .orElseThrow(() -> new PayeeNotFoundException()
                        .addField(PAYEE, requestData.payee()));

        Account accountLogged = (Account) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        if (!accountLogged.getId().equals(payer.getId())) {
            throw new TransactionNotAuthorizedException()
                    .addField(PAYER, requestData.payer())
                    .addField(PAYEE, requestData.payee())
                    .addField(AMOUNT, requestData.amount().setScale(2, RoundingMode.HALF_EVEN));
        }

        if (payer.getBalance().compareTo(requestData.amount()) < 0) {
            throw new BalanceInsuficientException(payer.getBalance(), requestData.amount())
                    .addField(BALANCE, payer.getBalance())
                    .addField(PAYER, requestData.payer())
                    .addField(PAYEE, requestData.payee())
                    .addField(AMOUNT, requestData.amount().setScale(2, RoundingMode.HALF_EVEN));
        }

        boolean isAuthorized = fraudPreventionService.authorize();

        if (!isAuthorized) {
            throw new TransactionNotAuthorizedException()
                    .addField(PAYER, requestData.payer())
                    .addField(PAYEE, requestData.payee())
                    .addField(AMOUNT, requestData.amount().setScale(2, RoundingMode.HALF_EVEN));
        }

        var newTransaction = Transaction.builder()
                .amount(requestData.amount())
                .payer(payer)
                .payee(payee)
                .build();

        payer.setBalance(payer.getBalance().subtract(requestData.amount()));
        payee.setBalance(payee.getBalance().add(requestData.amount()));

        Transaction createdTransaction = transactionRepository.save(newTransaction);
        entityManager.flush();

        return new TransactionDataResponse(createdTransaction);
    }
}
