package com.Mastra.banking.dto.request;

import java.math.BigDecimal;

public record TransferRequest(
    String accountNum,
    String relatedAccountNum,
    BigDecimal amount
) {}
