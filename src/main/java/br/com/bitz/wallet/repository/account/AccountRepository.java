package br.com.bitz.wallet.repository.account;

import br.com.bitz.wallet.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {

    UserDetails getByEmail(final String email);

    Optional<Account> findById(final String id);

    Optional<Account> findByDocument(final String id);

    Optional<Account> findByEmail(final String email);
}
