package br.com.bitz.wallet.domain.model.request;

import br.com.bitz.wallet.annotations.constraints.EnumValues;
import br.com.bitz.wallet.domain.enums.AccountType;
import br.com.bitz.wallet.annotations.constraints.CPForCNPJ;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

public record AccountRegisterDataRequest(
        @Length(min = 3, max = 120)
        String fullName,
        @CPForCNPJ
        String document,
        @Email
        String email,

        @Pattern(regexp = "\\d{6}",
                message = "{br.com.bitz.wallet.domain.model.request.AccountRegisterDataRequest.message}")
        String password,

        @EnumValues(value = AccountType.class, acceptableValues = "PF,PJ")
        String accountType) {
}
