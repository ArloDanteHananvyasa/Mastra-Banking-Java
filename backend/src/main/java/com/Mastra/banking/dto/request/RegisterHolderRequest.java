package com.Mastra.banking.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterHolderRequest(

    @NotBlank
    String name,
    
    @NotBlank
    String pob,
    
    @NotNull
    @Past
    LocalDate dob,

    @NotBlank
    @Pattern(regexp = "^08[0-9]{10,15}$")
    String phone,

    @NotBlank
    @Pattern(regexp = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\\b")
    String email,

    @NotBlank
    @Size(min = 8)
    String password
) {}
