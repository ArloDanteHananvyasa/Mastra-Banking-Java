package com.Mastra.banking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@SQLRestriction("deletedAt IS NULL")
public class Holder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long holderId;

    @NotBlank
    @Column(nullable = false, length = 200)
    private String name;
    
    @NotBlank
    @Column(nullable = false, length = 20, unique = true)
    private String phone;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @NotBlank
    @Column(nullable = false)
    private String pob;

    @NotBlank
    @Column(nullable = false)
    private LocalDate dob;

    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "holder", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Account> accounts = new ArrayList<Account>();

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
    }

}