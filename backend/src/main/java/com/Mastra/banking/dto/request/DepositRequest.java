package com.Mastra.banking.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record DepositRequest(

    @NotBlank
    Long accountId,
    
    @NotBlank
    @Positive
    BigDecimal amount

) {}
