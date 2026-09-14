package com.Mastra.banking.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Mastra.banking.dto.request.CreateAccountRequest;
import com.Mastra.banking.dto.response.AccountCreationResponse;
import com.Mastra.banking.dto.response.AccountResponse;
import com.Mastra.banking.dto.response.TransactionHistoryResponse;
import com.Mastra.banking.model.CustomUserDetails;
import com.Mastra.banking.service.AccountService;
import com.Mastra.banking.service.TransactionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    
    private final AccountService accountService;
    private final TransactionService transactionService;

    @GetMapping("/")
    public ResponseEntity<List<AccountResponse>> getAccounts(@AuthenticationPrincipal CustomUserDetails userDetails) {
        
        List<AccountResponse> response = accountService.getAccounts(userDetails.getUsername());

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(response);
    }

    @PostMapping("/create")
    public ResponseEntity<AccountCreationResponse> createAccount(@Valid @RequestBody CreateAccountRequest request) {

        AccountCreationResponse response = accountService.createAccount(request);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<List<TransactionHistoryResponse>> getTransactionHistory(@Valid @PathVariable Long accountId, @AuthenticationPrincipal CustomUserDetails userDetails) {

        List<TransactionHistoryResponse> response = transactionService.getTransactionHistory(accountId, userDetails.getUsername());

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(response);
    }
    

    


}
