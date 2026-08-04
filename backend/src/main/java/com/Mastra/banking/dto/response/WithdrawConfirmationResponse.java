package com.Mastra.banking.dto.response;

import java.math.BigDecimal;

public record WithdrawConfirmationResponse(
    Long transactionId,
    BigDecimal amount,
    BigDecimal newBalance
) {}
