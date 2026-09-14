package com.Mastra.banking.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Mastra.banking.dto.request.LoginRequest;
import com.Mastra.banking.dto.request.RegisterHolderRequest;
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



}