package com.Mastra.banking.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long account_id;
    
    @NotBlank
    @Column(nullable = false, unique = true)
    private String account_num;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(nullable = false)
    private BigDecimal balance;

    private enum Status {ACTIVE, CLOSED;}
    @Enumerated(EnumType.STRING)
    private Status status;
 
    private LocalDateTime created_at;
    private LocalDateTime deleted_at;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "holder_id", nullable = false)
    private Holder holder;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Transaction> transactions = new ArrayList<Transaction>();

    @OneToMany(mappedBy = "relatedAccount")
    private List<Transaction> related_transactions = new ArrayList<Transaction>();

    @PrePersist
    protected void onCreate(){
        this.created_at = LocalDateTime.now();
        if(this.status == null) {
            this.status = Status.ACTIVE;
        }
    }
}
