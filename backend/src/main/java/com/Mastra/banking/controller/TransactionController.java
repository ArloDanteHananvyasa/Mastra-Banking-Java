package com.Mastra.banking.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Mastra.banking.dto.request.DepositRequest;
import com.Mastra.banking.dto.request.TransferRequest;
import com.Mastra.banking.dto.request.WithdrawRequest;
import com.Mastra.banking.dto.response.DepositConfirmationResponse;
import com.Mastra.banking.dto.response.TransferConfirmationResponse;
import com.Mastra.banking.dto.response.WithdrawConfirmationResponse;
import com.Mastra.banking.model.CustomUserDetails;
import com.Mastra.banking.service.TransactionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {
    
    private final TransactionService transactionService;

    @PostMapping("/withdraw")
    public ResponseEntity<WithdrawConfirmationResponse> withdraw(
        @Valid @RequestBody WithdrawRequest request, 
        @AuthenticationPrincipal CustomUserDetails userDetails) {

        WithdrawConfirmationResponse response = transactionService.withdraw(request, userDetails.getUsername());

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(response);
    }

    @PostMapping("/deposit")
    public ResponseEntity<DepositConfirmationResponse> deposit(
        @Valid @RequestBody DepositRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {

        DepositConfirmationResponse response = transactionService.deposit(request, userDetails.getUsername());

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(response);
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransferConfirmationResponse> transfer(
        @Valid @RequestBody TransferRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {

        TransferConfirmationResponse response = transactionService.transfer(request, userDetails.getUsername());

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(response);
    }


}
