package br.com.bitz.wallet.config.security.filter;

import br.com.bitz.wallet.exception.ErrorsCode;
import br.com.bitz.wallet.repository.AccountRepository;
import br.com.bitz.wallet.service.port.JWTTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private final AccountRepository accountRepository;

    private final JWTTokenService tokenService;

    private final FilterExceptionHandler filterExceptionHandler;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain)
            throws ServletException, IOException {

      try {
          String token = this.getBearerToken(request);

          if (StringUtils.isNotEmpty(token)) {
              var email = tokenService.isValid(token);
              UserDetails user = accountRepository.getByEmail(email);

             if(Objects.nonNull(user)) {
                 var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                 SecurityContextHolder.getContext().setAuthentication(authentication);
             } else {
                 throw new UsernameNotFoundException("User not found.");
             }
          }

          filterChain.doFilter(request, response);
      } catch (Exception ex) {
          var problem =  filterExceptionHandler.handleException(ex, request);
          String responseBodyJson = objectMapper.writeValueAsString(problem);

          response.setStatus(ErrorsCode.valueOf((String) problem.get("code")).getHttpStatusCode().value());
          response.setContentType("application/json;charset=UTF-8");
          response.getWriter().write(responseBodyJson);
          response.getWriter().flush();
          response.getWriter().close();
      }
    }

    private String getBearerToken(final HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (StringUtils.isNotEmpty(token) && StringUtils.startsWith(token, "Bearer ")) {
            return token.replace("Bearer ", "");
        }

       return null;

    }
}
