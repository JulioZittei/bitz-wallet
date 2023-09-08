package br.com.bitz.wallet.domain.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record AuthDataRequest(
        @Email
        String email,

        @Pattern(regexp = "\\d{6}",
                message = "{br.com.bitz.wallet.domain.model.request.AuthDataRequest.message}")
        String password) {
}
