package br.com.bitz.wallet.service.port;

import br.com.bitz.wallet.domain.model.request.AccountRegisterDataRequest;
import br.com.bitz.wallet.domain.model.response.AccountDataResponse;

public interface CreateAccountService {

    AccountDataResponse execute(final AccountRegisterDataRequest requestData);
}
