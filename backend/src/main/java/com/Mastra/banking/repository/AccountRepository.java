package com.Mastra.banking.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Mastra.banking.model.Account;
import com.Mastra.banking.model.Holder;

public interface AccountRepository extends JpaRepository<Account, Long>{
    
    Optional<Account> findByAccountNum(String accountNum);

    List<Account> findByStatus(Account.Status status);

    List<Account> findByCreatedAt(LocalDateTime createdAt);

    List<Account> findByHolder(Holder holder);

    Optional<Account> findByAccountNumAndDeletedAtIsNull(String accountNum);
    
    List<Account> findByHolderAndDeletedAtIsNull(Holder holder);
}
