package br.com.bitz.wallet.controller;

import br.com.bitz.wallet.domain.model.request.AccountRegisterDataRequest;
import br.com.bitz.wallet.domain.model.request.AuthDataRequest;
import br.com.bitz.wallet.domain.model.response.AccountDataResponse;
import br.com.bitz.wallet.domain.model.response.TokenDataResponse;
import br.com.bitz.wallet.service.port.AuthService;
import br.com.bitz.wallet.service.port.CreateAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final CreateAccountService createAccountService;

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AccountDataResponse> createAccount(@RequestBody @Valid final AccountRegisterDataRequest requestData,
                                                             UriComponentsBuilder uriBuilder) {
        var uri = uriBuilder.path("/api/v1/accounts/me").build().toUri();
        return ResponseEntity.created(uri).body(createAccountService.execute(requestData));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<TokenDataResponse> authenticateAccount(@RequestBody @Valid final AuthDataRequest requestData) {
       return ResponseEntity.ok(authService.execute(requestData));
    }
}
