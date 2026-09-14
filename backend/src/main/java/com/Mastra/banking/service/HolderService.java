package com.Mastra.banking.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.util.file.ConfigurationSource.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Mastra.banking.dto.request.DeleteRequest;
import com.Mastra.banking.dto.request.LoginRequest;
import com.Mastra.banking.dto.request.RegisterHolderRequest;
import com.Mastra.banking.dto.response.DeleteConfirmationResponse;
import com.Mastra.banking.dto.response.HolderResponse;
import com.Mastra.banking.dto.response.LoginResponse;
import com.Mastra.banking.dto.response.RegistrationResponse;
import com.Mastra.banking.model.Holder;
import com.Mastra.banking.repository.HolderRepository;
import com.Mastra.banking.util.JwtUtil;
import com.Mastra.banking.util.exception.DuplicateEmailException;
import com.Mastra.banking.util.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HolderService {
    
    private final HolderRepository holderRepository;
    private final PasswordEncoder encoder;
    private final JwtUtil util;

    public RegistrationResponse register(RegisterHolderRequest request) {

        if (holderRepository.findByEmail(request.email()).isPresent()) {
            throw new DuplicateEmailException("Email already registered");
        }

        Holder holder = new Holder();
        holder.setName(request.name());
        holder.setEmail(request.email());
        holder.setDob(request.dob());
        holder.setPob(request.pob());
        holder.setPhone(request.phone());
        holder.setPassword(encoder.encode(request.password()));

        holderRepository.save(holder);

        return new RegistrationResponse(
            holder.getHolderId(),
            holder.getName(),
            holder.getEmail()
        );
    }

    public LoginResponse login(LoginRequest request) {

        Holder currentHolder = holderRepository.findByEmail(request.email())
            .orElseThrow(() -> new ResourceNotFoundException("No account found under this email"));


        if (encoder.matches(request.password(), currentHolder.getPassword())) {

            String token = util.generateToken(request.email(), "HOLDER");

            return new LoginResponse(
                currentHolder.getHolderId(),
                currentHolder.getName(),
                currentHolder.getEmail(),
                token
            );
        } else {
            throw new ResourceNotFoundException("Incorrect Login Credentials!");
        }

        
    }

    //need to add a logout handler

    public DeleteConfirmationResponse deleteHolder(DeleteRequest request) {
        
        Holder currentHolder = holderRepository.findById(request.id())
            .orElseThrow(() -> new ResourceNotFoundException("No account found under this email"));

        currentHolder.setDeletedAt(LocalDateTime.now());

        holderRepository.save(currentHolder);

        return new DeleteConfirmationResponse(
            request.id(),
            "Holder has successfully been deleted"
        );


    }

    public HolderResponse getHolder(String email) {
        
        Holder currentHolder = holderRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("No account found under this email"));

        return new HolderResponse(
            currentHolder.getHolderId(),
            currentHolder.getName(),
            currentHolder.getEmail(),
            currentHolder.getPhone(),
            currentHolder.getPob(),
            currentHolder.getDob()
        );
    }

    public List<HolderResponse> getAllHolders() {

        List<Holder> holders = holderRepository.findAll();

        List<HolderResponse> response = new ArrayList<HolderResponse>();

        for (Holder h : holders) {
            response.add(new HolderResponse(
                h.getHolderId(),
                h.getName(),
                h.getEmail(),
                h.getPhone(),
                h.getPob(),
                h.getDob()
            ));
        }

        return response;
    }
}
