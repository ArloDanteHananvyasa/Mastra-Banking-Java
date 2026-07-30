package com.Mastra.banking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Mastra.banking.model.Holder;

public interface HolderRepository extends JpaRepository<Holder, Long>{
    
}
