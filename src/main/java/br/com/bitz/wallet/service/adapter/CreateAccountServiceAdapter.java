package br.com.bitz.wallet.service.adapter;

import br.com.bitz.wallet.domain.entity.Account;
import br.com.bitz.wallet.domain.model.request.AccountRegisterDataRequest;
import br.com.bitz.wallet.domain.model.response.AccountDataResponse;
import br.com.bitz.wallet.exception.AccountConflictException;
import br.com.bitz.wallet.repository.account.AccountRepository;
import br.com.bitz.wallet.service.port.CreateAccountService;
import br.com.bitz.wallet.util.PasswordEncoderUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateAccountServiceAdapter implements CreateAccountService {

    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public AccountDataResponse execute(final AccountRegisterDataRequest requestData) {
        log.info("Checking if exists an account with email {}", requestData.email());
        accountRepository.findByEmail(requestData.email()).ifPresent(acc -> {
            throw new AccountConflictException(new String[]{"email", requestData.email()});
        });

        log.info("Checking if exists an account with document {}", requestData.document());
        accountRepository.findByDocument(requestData.document()).ifPresent(acc -> {
            throw new AccountConflictException(new String[]{"document", acc.getDocument()});
        });

        var newAccount = new Account(requestData);
        log.info("Encrypting account password", requestData.email());
        newAccount.setPassword(PasswordEncoderUtil.encodePassword(requestData.password()));

        log.info("Saving account", requestData.email());
        var createdAccount = accountRepository.save(newAccount);

        log.info("Returning created account", requestData.email());
        return new AccountDataResponse(createdAccount);
    }
}
