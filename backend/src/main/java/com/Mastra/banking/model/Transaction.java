package com.Mastra.banking.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@SQLRestriction("deletedAt IS NULL")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    private LocalDateTime timeStamp;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(nullable = false)
    private BigDecimal amount;

    private enum Type {DEPOSIT, WITHDRAWAL, TRANSFER_OUT, TRANSFER_IN;}
    @Enumerated(EnumType.STRING)
    private Type type;

    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_account_id", nullable = false)
    private Account relatedAccount;

    @PrePersist
    protected void onCreate(){
        this.timeStamp = LocalDateTime.now();
    }
}
