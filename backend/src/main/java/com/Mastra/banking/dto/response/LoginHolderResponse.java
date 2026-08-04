package com.Mastra.banking.dto.response;

public record LoginHolderResponse(
    Long holderId,
    String name,
    String email
    //JWT token, i don't know how to implement this yet
) {}
