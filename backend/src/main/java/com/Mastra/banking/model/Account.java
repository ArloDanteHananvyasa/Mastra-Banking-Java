package com.Mastra.banking.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@SQLRestriction("deletedAt IS NULL")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;
    
    @NotBlank
    @Column(nullable = false, unique = true)
    private String accountNum;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(nullable = false)
    private BigDecimal balance;
 
    @Enumerated(EnumType.STRING)
    private Status status;
 
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "holderId", nullable = false)
    private Holder holder;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Transaction> transactions = new ArrayList<Transaction>();

    @OneToMany(mappedBy = "relatedAccount")
    private List<Transaction> relatedTransactions = new ArrayList<Transaction>();

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
        if(this.status == null) {
            this.status = Status.ACTIVE;
        }
    }

    public enum Status {
        ACTIVE, CLOSED;
    }
}
