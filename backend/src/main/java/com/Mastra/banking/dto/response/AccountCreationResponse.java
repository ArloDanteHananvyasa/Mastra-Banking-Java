package com.Mastra.banking.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record AccountCreationResponse(
    
    @NotEmpty
    Long accountId,

    @NotBlank
    String accountNum
) {} 
