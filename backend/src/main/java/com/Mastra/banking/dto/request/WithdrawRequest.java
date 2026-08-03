package com.Mastra.banking.dto.request;

import java.math.BigDecimal;

public record WithdrawRequest(
    String accountNum,
    BigDecimal amount 
) {}
