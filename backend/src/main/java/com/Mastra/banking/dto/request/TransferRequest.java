package com.Mastra.banking.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record TransferRequest(

    @NotBlank
    Long fromAccount,
    
    @NotBlank
    String toAccountNum,
    
    @NotBlank
    @Positive
    BigDecimal amount
) {}
