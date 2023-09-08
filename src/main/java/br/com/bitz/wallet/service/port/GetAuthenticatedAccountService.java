package br.com.bitz.wallet.service.port;

import br.com.bitz.wallet.domain.entity.Account;

public interface GetAuthenticatedAccountService {
    Account execute(String email);
}
