package com.Mastra.banking.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.Mastra.banking.model.Transaction;

public record TransactionHistoryResponse(

    Long transactionId,
    LocalDateTime timeStamp,
    BigDecimal amount,
    Transaction.Type type,
    String relatedAccountNum

) {}