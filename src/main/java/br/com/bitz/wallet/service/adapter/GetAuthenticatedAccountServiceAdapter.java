package br.com.bitz.wallet.service.adapter;

import br.com.bitz.wallet.domain.entity.Account;
import br.com.bitz.wallet.exception.AccountNotFoundException;
import br.com.bitz.wallet.repository.AccountRepository;
import br.com.bitz.wallet.service.port.GetAuthenticatedAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAuthenticatedAccountServiceAdapter implements GetAuthenticatedAccountService {

    private final AccountRepository accountRepository;
    @Override
    public Account execute(final String email) {
        return accountRepository.findByEmail(email).orElseThrow(()-> {
            throw new AccountNotFoundException();
        });
    }
}
