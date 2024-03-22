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
import br.com.bitz.wallet.service.port.CreateTransactionService;
import br.com.bitz.wallet.service.port.FraudPreventionService;
import br.com.bitz.wallet.service.port.NotificationService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateTransactionServiceAdapter implements CreateTransactionService {

    private final EntityManager entityManager;

    private final TransactionRepository transactionRepository;

    private final AccountRepository accountRepository;

    private final FraudPreventionService fraudPreventionService;

    private final NotificationService notificationService;

    private static final String PAYER = "payer";
    private static final String PAYEE = "payee";
    private static final String AMOUNT = "amount";
    private static final String BALANCE = "balance";

    @Override
    @Transactional
    public TransactionDataResponse execute(final TransactionDataRequest requestData) {
        log.info("Checking if exists a payer account with id {}", requestData.payer());
        Account payer = accountRepository.findById(requestData.payer())
                .orElseThrow(() -> new PayerNotFoundException()
                        .addField(PAYER, requestData.payer()));

        log.info("Checking if exists a payee account with id {}", requestData.payee());
        Account payee = accountRepository.findById(requestData.payee())
                .orElseThrow(() -> new PayeeNotFoundException()
                        .addField(PAYEE, requestData.payee()));

        Account accountLogged = (Account) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        log.info("Checking if the account logged is the payer");
        if (!accountLogged.getId().equals(payer.getId())) {
            throw new TransactionNotAuthorizedException()
                    .addField(PAYER, requestData.payer())
                    .addField(PAYEE, requestData.payee())
                    .addField(AMOUNT, requestData.amount().setScale(2, RoundingMode.HALF_EVEN));
        }

        log.info("Checking if payer has enough balance");
        if (payer.getBalance().compareTo(requestData.amount()) < 0) {
            throw new BalanceInsuficientException(new Object[]{payer.getBalance(), requestData.amount()})
                    .addField(BALANCE, payer.getBalance())
                    .addField(PAYER, requestData.payer())
                    .addField(PAYEE, requestData.payee())
                    .addField(AMOUNT, requestData.amount().setScale(2, RoundingMode.HALF_EVEN));
        }

        log.info("Requesting fraud prevention service");
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

        log.info("Updating balance of payer account");
        payer.setBalance(payer.getBalance().subtract(requestData.amount()));

        log.info("Updating balance of payee account");
        payee.setBalance(payee.getBalance().add(requestData.amount()));

        log.info("Saving transaction");
        Transaction createdTransaction = transactionRepository.save(newTransaction);
        entityManager.flush();

        var response = new TransactionDataResponse(createdTransaction);

        log.info("Notifying transaction");
        notificationService.execute(response);

        log.info("Returning transaction created");
        return response;
    }
}
