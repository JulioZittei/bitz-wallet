package br.com.bitz.wallet.repository.transaction.output;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;


@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class TransactionOutput {

        @Id
        @EqualsAndHashCode.Include
        private String id;

        @Column(name = "amount")
        private BigDecimal amount;

        @Column(name = "payer_id")
        private String payer;

        @Column(name = "payee_id")
        private String payee;

        @Column(name = "created_at")
        private OffsetDateTime createdAt;

}
