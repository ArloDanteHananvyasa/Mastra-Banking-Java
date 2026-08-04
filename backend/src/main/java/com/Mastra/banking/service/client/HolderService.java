package com.Mastra.banking.service.client;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Mastra.banking.dto.request.DeleteRequest;
import com.Mastra.banking.dto.request.LoginRequest;
import com.Mastra.banking.dto.request.RegisterHolderRequest;
import com.Mastra.banking.dto.response.DeleteConfirmationResponse;
import com.Mastra.banking.dto.response.LoginHolderResponse;
import com.Mastra.banking.model.Account;
import com.Mastra.banking.model.Holder;
import com.Mastra.banking.repository.HolderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HolderService {
    
    private final HolderRepository holderRepository;
    private final PasswordEncoder encoder;

    public LoginHolderResponse register(RegisterHolderRequest request) {

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

        return new LoginHolderResponse(
            holder.getHolderId(),
            holder.getName(),
            holder.getEmail()
            //JWT Token should be here i'm guessing
        );
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

    public DeleteConfirmationResponse deleteAccount(DeleteRequest request) {
        
        Holder currentHolder = new Holder();

        if (!holderRepository.findById(request.id()).isPresent()) {
            throw new RuntimeException("No Holder found");
        } 
        else {
            currentHolder = holderRepository.findById(request.id()).get();
        }

        currentHolder.setDeletedAt(LocalDateTime.now());

        holderRepository.save(currentHolder);

        return new DeleteConfirmationResponse(
            request.id(),
            "Holder has successfully been deleted"
        );


    }
}
