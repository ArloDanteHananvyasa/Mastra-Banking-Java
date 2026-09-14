package com.Mastra.banking.service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.Mastra.banking.dto.request.CreateAccountRequest;
import com.Mastra.banking.dto.request.DeleteRequest;
import com.Mastra.banking.dto.response.AccountCreationResponse;
import com.Mastra.banking.dto.response.AccountResponse;
import com.Mastra.banking.dto.response.DeleteConfirmationResponse;
import com.Mastra.banking.model.Account;
import com.Mastra.banking.model.Holder;
import com.Mastra.banking.repository.AccountRepository;
import com.Mastra.banking.repository.HolderRepository;
import com.Mastra.banking.util.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final HolderRepository holderRepository;

    public AccountCreationResponse createAccount(CreateAccountRequest request) {
        
        Account newAccount = new Account();
        newAccount.setHolder(holderRepository.findById(request.holderId()).get());

        String accountNumber;

        do {
            accountNumber = generateAccountNumber();
        } while (accountRepository.existsByAccountNum(accountNumber));

        newAccount.setAccountNum(accountNumber);

        Account saved = accountRepository.save(newAccount);

        return new AccountCreationResponse(
            saved.getAccountId(),
            saved.getAccountNum()
        );
    }

    public DeleteConfirmationResponse deleteAccount(DeleteRequest request) {
        
        Account currentAccount = accountRepository.findById(request.id())
            .orElseThrow(() -> new ResourceNotFoundException("Account is not found"));

        currentAccount.setDeletedAt(LocalDateTime.now());

        accountRepository.save(currentAccount);

        return new DeleteConfirmationResponse(
            request.id(),
            "Account has successfully been deleted"
        );


    }

    public List<AccountResponse> getAccounts(String currentEmail) {

        Holder currentHolder = holderRepository.findByEmail(currentEmail)
            .orElseThrow(() -> new ResourceNotFoundException("Holder not found"));

        List<Account> accounts = accountRepository.findByHolder(currentHolder);

        List<AccountResponse> accResponse = new ArrayList<AccountResponse>();
        for (Account a : accounts) {
            accResponse.add(new AccountResponse(
                a.getAccountId(),
                a.getAccountNum(),
                a.getBalance(),
                a.getStatus()
            ));
        }

        return accResponse;    
    }

    public List<AccountResponse> getAccounts(Long holderId) {

        Holder currentHolder = holderRepository.findById(holderId)
            .orElseThrow(() -> new ResourceNotFoundException("Holder not found"));

        List<Account> accounts = accountRepository.findByHolder(currentHolder);

        List<AccountResponse> accResponse = new ArrayList<AccountResponse>();
        for (Account a : accounts) {
            accResponse.add(new AccountResponse(
                a.getAccountId(),
                a.getAccountNum(),
                a.getBalance(),
                a.getStatus()
            ));
        }

        return accResponse;    
    }

    private String generateAccountNumber() {
        SecureRandom randomizer = new SecureRandom();

        StringBuilder builder = new StringBuilder();

        builder.append(randomizer.nextInt(9) + 1);

        for (int i = 1; i <= 11; i++) {
            builder.append(randomizer.nextInt(10));
        }

        return builder.toString();
    }

    
}
