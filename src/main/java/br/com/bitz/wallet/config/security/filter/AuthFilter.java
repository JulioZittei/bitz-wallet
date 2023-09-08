package br.com.bitz.wallet.config.security.filter;

import br.com.bitz.wallet.repository.AccountRepository;
import br.com.bitz.wallet.service.port.JWTTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private final AccountRepository accountRepository;

    private final JWTTokenService tokenService;

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {

        String token = this.getBearerToken(request);

        if (Objects.nonNull(token)) {
            var email = tokenService.isValid(token);
            UserDetails user = accountRepository.findByEmail(email);

            var authentication = new UsernamePasswordAuthenticationToken(user, null, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String getBearerToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (StringUtils.isNotEmpty(token) && StringUtils.startsWith(token, "Bearer ")) {
            return token.split(" ")[1];
        }

       return null;

    }
}
