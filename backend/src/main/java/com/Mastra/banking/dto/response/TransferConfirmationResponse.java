package com.Mastra.banking.dto.response;

import java.math.BigDecimal;

public record TransferConfirmationResponse(
    Long transactionId,
    String relatedAccountNum,
    BigDecimal amount
) {}
