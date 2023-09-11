package br.com.bitz.wallet.controller;

import br.com.bitz.wallet.domain.model.request.AccountRegisterDataRequest;
import br.com.bitz.wallet.domain.model.request.AuthDataRequest;
import br.com.bitz.wallet.domain.model.response.AccountDataResponse;
import br.com.bitz.wallet.domain.model.response.TokenDataResponse;
import br.com.bitz.wallet.service.port.AuthService;
import br.com.bitz.wallet.service.port.CreateAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final CreateAccountService createAccountService;

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AccountDataResponse> createAccount(@RequestBody @Valid final AccountRegisterDataRequest requestData,
                                                             UriComponentsBuilder uriBuilder) {
        log.info("Requesting to create an account");
        var uri = uriBuilder.path("/api/v1/accounts/me").build().toUri();
        var createdAccount = createAccountService.execute(requestData);
        log.info("Responding created account");
        return ResponseEntity.created(uri).body(createdAccount);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<TokenDataResponse> authenticateAccount(@RequestBody @Valid final AuthDataRequest requestData) {
        log.info("Requesting to authenticate account");
        var token = authService.execute(requestData);
        log.info("Responding with access token");
        return ResponseEntity.ok(token);
    }
}
