package com.Mastra.banking.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record WithdrawRequest(
    
    @NotNull
    Long accountId,

    @NotNull
    @Positive
    BigDecimal amount 
) {}
