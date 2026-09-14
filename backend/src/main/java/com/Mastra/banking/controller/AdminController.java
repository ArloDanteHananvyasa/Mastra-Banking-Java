package com.Mastra.banking.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Mastra.banking.dto.request.DeleteRequest;
import com.Mastra.banking.dto.request.LoginRequest;
import com.Mastra.banking.dto.response.AccountResponse;
import com.Mastra.banking.dto.response.DeleteConfirmationResponse;
import com.Mastra.banking.dto.response.HolderResponse;
import com.Mastra.banking.dto.response.LoginResponse;
import com.Mastra.banking.dto.response.TransactionHistoryResponse;
import com.Mastra.banking.service.AccountService;
import com.Mastra.banking.service.AdminService;
import com.Mastra.banking.service.HolderService;
import com.Mastra.banking.service.TransactionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    
    private final AdminService adminService;
    private final HolderService holderService;
    private final AccountService accountService;
    private final TransactionService transactionService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        
        LoginResponse response = adminService.login(request);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(response);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/holders")
    public ResponseEntity<List<HolderResponse>> getHolders() {
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(holderService.getAllHolders());
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/holders/delete/{id}")
    public ResponseEntity<DeleteConfirmationResponse> deleteHolder(@PathVariable Long id) {

        DeleteRequest request = new DeleteRequest(id);

        DeleteConfirmationResponse response = holderService.deleteHolder(request);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/holders/{holderId}/accounts")
    public ResponseEntity<List<AccountResponse>> getAccounts(@PathVariable Long holderId) {
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(accountService.getAccounts(holderId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/holders/{holderId}/accounts/delete/{id}")
    public ResponseEntity<DeleteConfirmationResponse> deleteAccount(@Valid @PathVariable Long id) {

        DeleteRequest request = new DeleteRequest(id);

        DeleteConfirmationResponse response = accountService.deleteAccount(request);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/holders/{holderId}/accounts/{accountId}/transactions")
    public ResponseEntity<List<TransactionHistoryResponse>> getTransactions(@PathVariable Long holderId, @PathVariable Long accountId) {
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(transactionService.getTransactionHistory(accountId, holderId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/holders/{holderId}/accounts/{accountId}/transactions/delete/{id}")
    public ResponseEntity<DeleteConfirmationResponse> deleteTransaction(@PathVariable Long id) {

        DeleteRequest request = new DeleteRequest(id);

        DeleteConfirmationResponse response = transactionService.deleteTransaction(request);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(response);
    }
}
