package com.Mastra.banking.dto.request;

import java.math.BigDecimal;

public record DepositRequest(
    Long accountId,
    BigDecimal amount
) {}
