package br.com.bitz.wallet.service.adapter;

import br.com.bitz.wallet.domain.entity.Account;
import br.com.bitz.wallet.exception.AccountNotFoundException;
import br.com.bitz.wallet.repository.account.AccountRepository;
import br.com.bitz.wallet.service.port.GetAuthenticatedAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetAuthenticatedAccountServiceAdapter implements GetAuthenticatedAccountService {

    private final AccountRepository accountRepository;
    @Override
    public Account execute(final String email) {
        log.info("Getting account by email {}", email);
        return accountRepository.findByEmail(email).orElseThrow(()-> {
            throw new AccountNotFoundException();
        });
    }
}
