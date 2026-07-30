package com.Mastra.banking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Mastra.banking.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long>{
    
}
