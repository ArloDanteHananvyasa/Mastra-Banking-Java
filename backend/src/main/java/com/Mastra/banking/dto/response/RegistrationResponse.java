package com.Mastra.banking.dto.response;

public record RegistrationResponse(
    Long holderId,
    String name,
    String email
) {}
