package com.Mastra.banking.service.client;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Mastra.banking.dto.request.LoginRequest;
import com.Mastra.banking.dto.request.RegisterHolderRequest;
import com.Mastra.banking.dto.response.LoginHolderResponse;
import com.Mastra.banking.model.Holder;
import com.Mastra.banking.repository.HolderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HolderService {
    
    private final HolderRepository holderRepository;
    private final PasswordEncoder encoder;

    public String register(RegisterHolderRequest request) {

        if (holderRepository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        Holder holder = new Holder();
        holder.setName(request.name());
        holder.setEmail(request.email());
        holder.setDob(request.dob());
        holder.setPob(request.pob());
        holder.setPhone(request.phone());
        holder.setPassword(encoder.encode(request.password()));

        holderRepository.save(holder);

        return "Registration Successfull";
    }

    public LoginHolderResponse login(LoginRequest request) {

        Holder currentHolder = new Holder();
        
        if (!holderRepository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("No Account registered under this email address");
        } 
        else {
            currentHolder = holderRepository.findByEmail(request.email()).get();
        }

        if (encoder.matches(currentHolder.getPassword(), request.password())) {
            return new LoginHolderResponse(
                currentHolder.getHolderId(),
                currentHolder.getName(),
                currentHolder.getEmail()
                //JWT Token should be here i'm guessing
            );
        } else {
            throw new RuntimeException("Incorrect Login Credentials!");
        }

        
    }

    //need to add a logout handler
}
