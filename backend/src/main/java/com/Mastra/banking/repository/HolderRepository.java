package com.Mastra.banking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Mastra.banking.model.Holder;

public interface HolderRepository extends JpaRepository<Holder, Long>{
    
    Optional<Holder> findByEmail(String email);

    Optional<Holder> findByPhone(String phone);
}
