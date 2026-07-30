package com.Mastra.banking.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transaction_id;

    private LocalDateTime time_stamp;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(nullable = false)
    private BigDecimal amount;

    private enum Type {DEPOSIT, WITHDRAWAL, TRANSFER_OUT, TRANSFER_IN;}
    @Enumerated(EnumType.STRING)
    private Type type;

    private LocalDateTime deleted_at;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_account_id", nullable = false)
    private Account relatedAccount;

    @PrePersist
    protected void onCreate(){
        this.time_stamp = LocalDateTime.now();
    }
}
