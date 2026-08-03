package com.Mastra.banking.dto.request;

import java.time.LocalDate;

public record RegisterHolderRequest(
    String name,
    String pob,
    LocalDate dob,
    String phone,
    String email,
    String password
) {}
