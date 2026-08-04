package com.Mastra.banking.dto.response;

import java.math.BigDecimal;

public record DepositConfirmationResponse(
    Long transactionId,
    BigDecimal amount,
    BigDecimal newBalance
) {}
