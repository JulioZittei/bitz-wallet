package br.com.bitz.wallet.service.port;

import br.com.bitz.wallet.domain.entity.Account;

public interface JWTTokenService {

    String isValid(final String token);

    String generateToken(final Account accountCredentials);
}
