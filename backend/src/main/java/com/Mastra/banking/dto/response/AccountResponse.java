package com.Mastra.banking.dto.response;

import java.math.BigDecimal;

import com.Mastra.banking.model.Account;

public record AccountResponse(

    Long accountId,
    String accountNum,
    BigDecimal balance,
    Account.Status status

) {}

