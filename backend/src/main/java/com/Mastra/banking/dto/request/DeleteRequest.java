package com.Mastra.banking.dto.request;

import jakarta.validation.constraints.NotBlank;

public record DeleteRequest(

    @NotBlank
    Long id

) {}
