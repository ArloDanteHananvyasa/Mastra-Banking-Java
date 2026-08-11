package com.Mastra.banking.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Mastra.banking.dto.request.LoginRequest;
import com.Mastra.banking.dto.response.LoginResponse;
import com.Mastra.banking.model.Admin;
import com.Mastra.banking.repository.AdminRepository;
import com.Mastra.banking.util.JwtUtil;
import com.Mastra.banking.util.exception.InvalidCredentialsException;
import com.Mastra.banking.util.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {
    
    private final AdminRepository adminRepository;
    private final PasswordEncoder encoder;
    private final JwtUtil util;

    public LoginResponse login(LoginRequest request) {

        Admin currentAdmin = adminRepository.findByEmail(request.email())
            .orElseThrow(() -> new ResourceNotFoundException("No account found under this email"));

        if (encoder.matches(request.password(), currentAdmin.getPassword())) {

            String token = util.generateToken(request.email(), "Admin");

            return new LoginResponse(
                currentAdmin.getAdminId(),
                currentAdmin.getName(),
                currentAdmin.getEmail(),
                token
            );
        } else {
            throw new InvalidCredentialsException("Incorrect Login Credentials!");
        }

        
    }

    //need to add a logout handler

}
