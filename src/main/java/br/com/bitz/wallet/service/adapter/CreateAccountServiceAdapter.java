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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateAccountServiceAdapter implements CreateAccountService {

    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public AccountDataResponse execute(final AccountRegisterDataRequest requestData) {
        accountRepository.findByEmail(requestData.email()).ifPresent(acc -> {
            throw new AccountConflictException("email", requestData.email());
        });

        accountRepository.findByDocument(requestData.document()).ifPresent(acc -> {
            throw new AccountConflictException("document", requestData.document());
        });

        var newAccount = new Account(requestData);
        newAccount.setPassword(PasswordEncoderUtil.encodePassword(requestData.password()));
        var createdAccount = accountRepository.save(newAccount);

        return new AccountDataResponse(createdAccount);
    }
}
