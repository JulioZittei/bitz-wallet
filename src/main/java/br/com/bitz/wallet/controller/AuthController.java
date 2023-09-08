package br.com.bitz.wallet.controller;

import br.com.bitz.wallet.domain.model.request.AccountRegisterDataRequest;
import br.com.bitz.wallet.domain.model.response.AccountDataResponse;
import br.com.bitz.wallet.service.port.RegisterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/auth/register")
@RequiredArgsConstructor
public class AuthController {

    private final RegisterService registerService;

    @PostMapping
    public ResponseEntity<AccountDataResponse> createAccount(@RequestBody @Valid final AccountRegisterDataRequest requestData,
                                                             UriComponentsBuilder uriBuilder) {
        var uri = uriBuilder.path("/api/v1/accounts/me").build().toUri();
        return ResponseEntity.created(uri).body(registerService.execute(requestData));
    }
}
