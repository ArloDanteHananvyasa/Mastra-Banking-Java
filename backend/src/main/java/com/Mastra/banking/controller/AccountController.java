package com.Mastra.banking.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Mastra.banking.dto.request.CreateAccountRequest;
import com.Mastra.banking.dto.request.DeleteRequest;
import com.Mastra.banking.dto.response.AccountCreationResponse;
import com.Mastra.banking.dto.response.DeleteConfirmationResponse;
import com.Mastra.banking.service.AccountService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {
    
    private final AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<AccountCreationResponse> createAccount(@Valid @RequestBody CreateAccountRequest request) {

        AccountCreationResponse response = accountService.createAccount(request);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<DeleteConfirmationResponse> deleteHolder(@Valid @PathVariable Long id) {

        DeleteRequest request = new DeleteRequest(id);

        DeleteConfirmationResponse response = accountService.deleteAccount(request);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(response);
    }
}
