package br.com.bitz.wallet.domain.model.response;

import br.com.bitz.wallet.domain.entity.Account;
import br.com.bitz.wallet.util.CPForCNPJUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record AccountDataResponse(
        String id,
        String fullName,
        String document,
        String email,
        BigDecimal balance,
        String accountType
) {

    public AccountDataResponse(final Account account) {

        this(account.getId(), account.getFullName(), CPForCNPJUtil.maskSecurity(account.getDocument()),
                account.getEmail(), account.getBalance().setScale(2, RoundingMode.HALF_EVEN), account.getAccountType().name());

    }
}
