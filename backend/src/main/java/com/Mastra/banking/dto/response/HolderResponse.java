package com.Mastra.banking.dto.response;

import java.time.LocalDate;

public record HolderResponse(

    Long holderId,
    String name,
    String email,
    String phone,
    String pob,
    LocalDate dob

) {}
