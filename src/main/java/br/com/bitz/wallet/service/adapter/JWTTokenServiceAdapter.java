package br.com.bitz.wallet.service.adapter;

import br.com.bitz.wallet.domain.entity.Account;
import br.com.bitz.wallet.service.port.JWTTokenService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class JWTTokenServiceAdapter implements JWTTokenService {

    private final String SECRET;

    private final String ISSUER;

    public JWTTokenServiceAdapter(@Value("${security.jwt.secret}") final String secret,
                                  @Value("${spring.application.issuer}") final String issuer) {
        this.SECRET = secret;
        this.ISSUER = issuer;
    }

    @Override
    public String isValid(final String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            return JWT.require(algorithm).
                    withIssuer(ISSUER)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String generateToken(final Account accountCredentials) {
        Instant expiration = LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.of("-03:00"));

        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withIssuedAt(Instant.now())
                    .withSubject(accountCredentials.getEmail())
                    .withExpiresAt(expiration)
                    .sign(algorithm);
        } catch (JWTCreationException ex) {
            throw new RuntimeException(ex);
        }
    }
}
