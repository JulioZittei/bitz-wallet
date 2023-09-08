package br.com.bitz.wallet.controller;

import br.com.bitz.wallet.domain.entity.Account;
import br.com.bitz.wallet.domain.model.response.AccountDataResponse;
import br.com.bitz.wallet.service.port.GetAuthenticatedAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final GetAuthenticatedAccountService getAuthenticatedAccountService;

    @GetMapping("/me")
    public ResponseEntity<AccountDataResponse> getAuthenticatedAccountData() {
        Account authenticatedAccount = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var account = getAuthenticatedAccountService.execute(authenticatedAccount.getEmail());
        return ResponseEntity.ok(new AccountDataResponse(account));
    }
}
