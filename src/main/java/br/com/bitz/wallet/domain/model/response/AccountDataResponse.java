package br.com.bitz.wallet.domain.model.response;

import br.com.bitz.wallet.domain.entity.Account;
import br.com.bitz.wallet.util.CPForCNPJUtil;

import java.util.UUID;

public record AccountDataResponse(
        UUID id,
        String fullName,
        String document,
        String email,
        String accountType
) {

    public AccountDataResponse(final Account account) {

        this(account.getId(), account.getFullName(), CPForCNPJUtil.maskSecurity(account.getDocument()),
                account.getEmail(), account.getAccountType().name());

    }
}
