package br.com.bitz.wallet.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@ToString
@Builder(setterPrefix = "")
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
@EqualsAndHashCode(of = "id")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "amount")
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "payer_id")
    private Account payer;

    @ManyToOne
    @JoinColumn(name = "payee_id")
    private Account payee;

    @CreationTimestamp
    @Column(name = "created_at")
    private OffsetDateTime createdAt;
}
