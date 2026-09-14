package com.Mastra.banking.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateAccountRequest(

    @NotBlank
    Long holderId
    
) {} 
