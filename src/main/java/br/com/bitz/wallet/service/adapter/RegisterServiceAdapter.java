package br.com.bitz.wallet.service.adapter;

import br.com.bitz.wallet.domain.entity.Account;
import br.com.bitz.wallet.domain.model.request.AccountRegisterDataRequest;
import br.com.bitz.wallet.domain.model.response.AccountDataResponse;
import br.com.bitz.wallet.repository.AccountRepository;
import br.com.bitz.wallet.service.port.RegisterService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterServiceAdapter implements RegisterService {

    private final AccountRepository accountRepository;



    @Override
    @Transactional
    public AccountDataResponse execute(final AccountRegisterDataRequest requestData) {
        final var newAccount = new Account(requestData);
        newAccount.setPassword(encodePassword(requestData.password()));
        final var createdAccount = accountRepository.save(newAccount);

        return new AccountDataResponse(createdAccount);
    }


    private String encodePassword(String password) {
        PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return new BCryptPasswordEncoder().encode(password);
    }

}
