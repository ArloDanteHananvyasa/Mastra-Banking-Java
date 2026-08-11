package com.Mastra.banking.controller;

import org.apache.catalina.connector.Response;
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
import com.Mastra.banking.dto.request.RegisterHolderRequest;
import com.Mastra.banking.dto.response.DeleteConfirmationResponse;
import com.Mastra.banking.dto.response.LoginResponse;
import com.Mastra.banking.dto.response.RegistrationResponse;
import com.Mastra.banking.service.HolderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/holder")
@RequiredArgsConstructor
public class HolderController {

    private final HolderService holderService;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@Valid @RequestBody RegisterHolderRequest request) {
        
        RegistrationResponse response = holderService.register(request);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {

        LoginResponse response = holderService.login(request);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<DeleteConfirmationResponse> deleteHolder(@PathVariable Long id) {

        DeleteRequest request = new DeleteRequest(id);

        DeleteConfirmationResponse response = holderService.deleteHolder(request);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(response);
    }

}