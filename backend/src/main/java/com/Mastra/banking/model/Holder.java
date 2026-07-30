package com.Mastra.banking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Holder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long holder_id;

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

    private LocalDateTime created_at;
    private LocalDateTime deleted_at;

    @OneToMany(mappedBy = "holder", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Account> accounts = new ArrayList<Account>();

    @PrePersist
    protected void onCreate(){
        this.created_at = LocalDateTime.now();
    }

}