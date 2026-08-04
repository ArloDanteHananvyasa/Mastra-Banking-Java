package com.Mastra.banking.service.client;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Mastra.banking.dto.request.LoginRequest;
import com.Mastra.banking.dto.response.LoginResponse;
import com.Mastra.banking.model.Admin;
import com.Mastra.banking.repository.AdminRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {
    
    private final AdminRepository adminRepository;
    private final PasswordEncoder encoder;

    public LoginResponse login(LoginRequest request) {

        Admin currentAdmin = new Admin();
        
        if (!adminRepository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("No Account registered under this email address");
        } 
        else {
            currentAdmin = adminRepository.findByEmail(request.email()).get();
        }

        if (encoder.matches(currentAdmin.getPassword(), request.password())) {
            return new LoginResponse(
                currentAdmin.getAdminId(),
                currentAdmin.getName(),
                currentAdmin.getEmail()
                //JWT Token should be here i'm guessing
            );
        } else {
            throw new RuntimeException("Incorrect Login Credentials!");
        }

        
    }

    //need to add a logout handler

}
