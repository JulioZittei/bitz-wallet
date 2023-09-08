package br.com.bitz.wallet.service.port;

import br.com.bitz.wallet.domain.model.request.AuthDataRequest;
import br.com.bitz.wallet.domain.model.response.TokenDataResponse;

public interface AuthService {
    TokenDataResponse execute(final AuthDataRequest requestData);
}
