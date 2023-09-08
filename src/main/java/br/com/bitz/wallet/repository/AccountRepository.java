package br.com.bitz.wallet.repository;

import br.com.bitz.wallet.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {

    UserDetails findByEmail(final String email);
}
