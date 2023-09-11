package br.com.bitz.wallet.service.adapter;

import br.com.bitz.wallet.domain.entity.Account;
import br.com.bitz.wallet.domain.model.request.AuthDataRequest;
import br.com.bitz.wallet.domain.model.response.TokenDataResponse;
import br.com.bitz.wallet.repository.account.AccountRepository;
import br.com.bitz.wallet.service.port.AuthService;
import br.com.bitz.wallet.service.port.JWTTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceAdapter implements UserDetailsService, AuthService {

    @Autowired
    private AuthenticationManager authManager;

    private final AccountRepository accountRepository;

    private final JWTTokenService jwtTokenService;

    @Override
    public TokenDataResponse execute(final AuthDataRequest requestData) {
        var usernameAndPassword = new UsernamePasswordAuthenticationToken(requestData.email(), requestData.password());
        try {
            var auth = this.authManager.authenticate(usernameAndPassword);
            String token = this.jwtTokenService.generateToken((Account) auth.getPrincipal());
            return new TokenDataResponse(token);
        } catch (InternalAuthenticationServiceException ex) {
            throw new BadCredentialsException("Invalid user and/or password");
        }
    }

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        return this.accountRepository.getByEmail(email);
    }
}
