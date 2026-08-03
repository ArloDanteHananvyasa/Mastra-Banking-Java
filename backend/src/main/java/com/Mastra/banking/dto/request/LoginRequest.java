package com.Mastra.banking.dto.request;

public record LoginRequest(
    String email,
    String password
) {}
