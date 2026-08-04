package com.Mastra.banking.service.client;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Mastra.banking.dto.request.DepositRequest;
import com.Mastra.banking.dto.request.TransferRequest;
import com.Mastra.banking.dto.request.WithdrawRequest;
import com.Mastra.banking.dto.response.DepositConfirmationResponse;
import com.Mastra.banking.dto.response.TransferConfirmationResponse;
import com.Mastra.banking.dto.response.WithdrawConfirmationResponse;
import com.Mastra.banking.model.Account;
import com.Mastra.banking.model.Transaction;
import com.Mastra.banking.model.Transaction.Type;
import com.Mastra.banking.repository.AccountRepository;
import com.Mastra.banking.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {
    
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public DepositConfirmationResponse deposit(DepositRequest request) {
        
        Account currentAccount = new Account();

        if (accountRepository.findById(request.accountId()).isPresent()) {
            throw new RuntimeException("Cannot find account with this number");
        }
        else {
            currentAccount = accountRepository.findById(request.accountId()).get();
        }
        
        BigDecimal newBalance = currentAccount.getBalance().add(request.amount());

        currentAccount.setBalance(newBalance);

        accountRepository.save(currentAccount);

        Transaction newTransaction = new Transaction();
        newTransaction.setAccount(currentAccount);
        newTransaction.setAmount(request.amount());
        newTransaction.setType(Type.DEPOSIT);

        Transaction saved = transactionRepository.save(newTransaction);

        return new DepositConfirmationResponse(
            saved.getTransactionId(),
            request.amount(),
            currentAccount.getBalance()
        );
    }

    @Transactional
    public WithdrawConfirmationResponse withdraw(WithdrawRequest request) {
        
        Account currentAccount = new Account();

        if (accountRepository.findById(request.accountId()).isPresent()) {
            throw new RuntimeException("Cannot find account with this number");
        }
        else {
            currentAccount = accountRepository.findById(request.accountId()).get();
        }

        BigDecimal newBalance = currentAccount.getBalance().subtract(request.amount());

        if (newBalance.compareTo(BigDecimal.ZERO) == -1) {
            throw new RuntimeException("DECLINED! Withdrawal cannot exceed account balance.");
        }

        currentAccount.setBalance(newBalance);

        accountRepository.save(currentAccount);

        Transaction newTransaction = new Transaction();
        newTransaction.setAccount(currentAccount);
        newTransaction.setAmount(request.amount());
        newTransaction.setType(Type.WITHDRAWAL);

        Transaction saved = transactionRepository.save(newTransaction);

        return new WithdrawConfirmationResponse(
            saved.getTransactionId(),
            request.amount(),
            currentAccount.getBalance()
        );
    } 

    @Transactional
    public TransferConfirmationResponse transfer(TransferRequest request) {

        Account fromAccount = new Account();
        if (accountRepository.findById(request.fromAccount()).isPresent()) {
            throw new RuntimeException("Cannot find account with this number");
        }
        else {
            fromAccount = accountRepository.findById(request.fromAccount()).get();
        }
        BigDecimal fromBalance = fromAccount.getBalance();

        if (fromBalance.compareTo(BigDecimal.ZERO) == -1) {
            throw new RuntimeException("DECLINED! Withdrawal cannot exceed account balance.");
        }
        
        Account toAccount = new Account();
        if (accountRepository.findByAccountNum(request.toAccountNum()).isPresent()) {
            throw new RuntimeException("Cannot find account with this number");
        }
        else {
            toAccount = accountRepository.findByAccountNum(request.toAccountNum()).get();
        }
        BigDecimal toBalance = toAccount.getBalance();

        fromAccount.setBalance(fromBalance.subtract(request.amount()));
        toAccount.setBalance(toBalance.add(request.amount()));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction fromTransaction = new Transaction();
        fromTransaction.setAccount(fromAccount);
        fromTransaction.setRelatedAccount(toAccount);
        fromTransaction.setAmount(request.amount());
        fromTransaction.setType(Type.TRANSFER_OUT);
    
        Transaction toTransaction = new Transaction();
        toTransaction.setAccount(toAccount);
        toTransaction.setRelatedAccount(fromAccount);
        toTransaction.setAmount(request.amount());
        toTransaction.setType(Type.TRANSFER_IN);

        Transaction saved = transactionRepository.save(fromTransaction);
        transactionRepository.save(toTransaction);

        return new TransferConfirmationResponse(
            saved.getTransactionId(),
            toAccount.getAccountNum(),
            request.amount()
        );

    }
}
