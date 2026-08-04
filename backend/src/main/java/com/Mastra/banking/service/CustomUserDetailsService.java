package com.Mastra.banking.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.Mastra.banking.model.Admin;
import com.Mastra.banking.model.CustomUserDetails;
import com.Mastra.banking.model.Holder;
import com.Mastra.banking.repository.AdminRepository;
import com.Mastra.banking.repository.HolderRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    
    private final AdminRepository adminRepository;
    private final HolderRepository holderRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        Holder holder = holderRepository.findByEmail(email).orElse(null);
        if (holder != null) {
            return new CustomUserDetails(holder.getEmail(), holder.getPassword(), "HOLDER");
        }

        Admin admin = adminRepository.findByEmail(email).orElse(null);
        if (admin != null) {
            return new CustomUserDetails(admin.getEmail(), admin.getPassword(), "ADMIN");
        }

        throw new UsernameNotFoundException("No user found with the email: " + email);
    }

    
}
