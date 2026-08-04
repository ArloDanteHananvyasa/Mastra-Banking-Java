package com.Mastra.banking.dto.request;

import java.math.BigDecimal;

public record TransferRequest(
    Long fromAccount,
    String toAccountNum,
    BigDecimal amount
) {}
