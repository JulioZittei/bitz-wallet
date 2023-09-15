package br.com.bitz.wallet.service.adapter;

import br.com.bitz.wallet.domain.entity.Account;
import br.com.bitz.wallet.domain.model.request.AuthDataRequest;
import br.com.bitz.wallet.domain.model.response.TokenDataResponse;
import br.com.bitz.wallet.repository.account.AccountRepository;
import br.com.bitz.wallet.service.port.AuthService;
import br.com.bitz.wallet.service.port.JWTTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceAdapter implements AuthService {

    private final AuthenticationManager authManager;

    private final AccountRepository accountRepository;

    private final JWTTokenService jwtTokenService;

    @Override
    public TokenDataResponse execute(final AuthDataRequest requestData) {
        var usernameAndPassword = new UsernamePasswordAuthenticationToken(requestData.email(), requestData.password());
        try {
            log.info("Authenticating account with email {}", requestData.email());
            var auth = authManager.authenticate(usernameAndPassword);

            log.info("Building access token");
            String token = jwtTokenService.generateToken((Account) auth.getPrincipal());

            log.info("Returning access token");
            return new TokenDataResponse(token);
        } catch (InternalAuthenticationServiceException ex) {
            log.error("Error while authenticating account");
            throw new BadCredentialsException("Invalid user and/or password");
        }
    }
}
