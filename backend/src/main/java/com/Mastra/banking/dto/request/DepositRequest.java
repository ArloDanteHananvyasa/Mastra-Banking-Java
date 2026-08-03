package com.Mastra.banking.dto.request;

import java.math.BigDecimal;

public record DepositRequest(
    String accountNum,
    BigDecimal amount
) {}
