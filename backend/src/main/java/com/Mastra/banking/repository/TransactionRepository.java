package com.Mastra.banking.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Mastra.banking.model.Account;
import com.Mastra.banking.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{
    
    List<Transaction> findByAccount(Account account);
    
    List<Transaction> findByAccountAndTimeStamp(Account account, LocalDateTime time_stamp);
    
    List<Transaction> findByAccountAndType(Account account, String type);
}
