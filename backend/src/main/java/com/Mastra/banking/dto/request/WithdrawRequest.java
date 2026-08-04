package com.Mastra.banking.dto.request;

import java.math.BigDecimal;

public record WithdrawRequest(
    Long accountId,
    BigDecimal amount 
) {}
