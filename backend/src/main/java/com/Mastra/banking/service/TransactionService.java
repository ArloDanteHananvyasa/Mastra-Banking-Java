package com.Mastra.banking.service;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Mastra.banking.dto.request.DeleteRequest;
import com.Mastra.banking.dto.request.DepositRequest;
import com.Mastra.banking.dto.request.TransferRequest;
import com.Mastra.banking.dto.request.WithdrawRequest;
import com.Mastra.banking.dto.response.DeleteConfirmationResponse;
import com.Mastra.banking.dto.response.DepositConfirmationResponse;
import com.Mastra.banking.dto.response.TransactionHistoryResponse;
import com.Mastra.banking.dto.response.TransferConfirmationResponse;
import com.Mastra.banking.dto.response.WithdrawConfirmationResponse;
import com.Mastra.banking.model.Account;
import com.Mastra.banking.model.Transaction;
import com.Mastra.banking.model.Transaction.Type;
import com.Mastra.banking.repository.AccountRepository;
import com.Mastra.banking.repository.TransactionRepository;
import com.Mastra.banking.util.exception.AccountAccessDeniedException;
import com.Mastra.banking.util.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {
    
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public DepositConfirmationResponse deposit(DepositRequest request, String currentEmail) {
        
        Account currentAccount = accountRepository.findById(request.accountId())
            .orElseThrow(() -> new ResourceNotFoundException("No account found under this number"));

        if (!currentAccount.getHolder().getEmail().equals(currentEmail)) {
            throw new AccountAccessDeniedException("You don't have access to this account");
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
    public WithdrawConfirmationResponse withdraw(WithdrawRequest request, String currentEmail) {
        
        Account currentAccount = accountRepository.findById(request.accountId())
            .orElseThrow(() -> new ResourceNotFoundException("No account found under this number"));

        if (!currentAccount.getHolder().getEmail().equals(currentEmail)) {
            throw new AccountAccessDeniedException("You don't have access to this account");
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
    public TransferConfirmationResponse transfer(TransferRequest request, String currentEmail) {

        Account fromAccount = accountRepository.findById(request.fromAccount())
            .orElseThrow(() -> new ResourceNotFoundException("No account found under this number"));

        if (!fromAccount.getHolder().getEmail().equals(currentEmail)) {
            throw new AccountAccessDeniedException("You don't have access to this account");
        }

        BigDecimal fromBalance = fromAccount.getBalance();

        if (fromBalance.subtract(request.amount()).compareTo(BigDecimal.ZERO) == -1) {
            throw new RuntimeException("DECLINED! Withdrawal cannot exceed account balance.");
        }
        
        Account toAccount = accountRepository.findByAccountNum(request.toAccountNum())
            .orElseThrow(() -> new ResourceNotFoundException("No account found under this number"));
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

    public DeleteConfirmationResponse deleteTransaction(DeleteRequest request) {
        
        Transaction currentTransaction = transactionRepository.findById(request.id())
            .orElseThrow(() -> new ResourceNotFoundException("No transaction found"));

        currentTransaction.setDeletedAt(LocalDateTime.now());

        transactionRepository.save(currentTransaction);

        return new DeleteConfirmationResponse(
            request.id(),
            "Transaction has successfully been deleted"
        );


    }

    public List<TransactionHistoryResponse> getTransactionHistory(Long account, String email) {
        Account currentAccount = accountRepository.findById(account).orElseThrow(() -> new ResourceNotFoundException("No account found"));

        if (!currentAccount.getHolder().getEmail().equals(email)) {
            throw new AccountAccessDeniedException("You don't have access to this account");
        }

        List<Transaction> transactions = transactionRepository.findByAccount(currentAccount);

        List<TransactionHistoryResponse> responses = new ArrayList<TransactionHistoryResponse>();

        for (Transaction t : transactions) {


            if (t.getType() == Transaction.Type.TRANSFER_IN || t.getType() == Transaction.Type.TRANSFER_OUT) {
                responses.add(new TransactionHistoryResponse(
                    t.getTransactionId(),
                    t.getTimeStamp(),
                    t.getAmount(),
                    t.getType(),
                    t.getRelatedAccount().getAccountNum()
                ));
            } else {
                responses.add(new TransactionHistoryResponse(
                    t.getTransactionId(),
                    t.getTimeStamp(),
                    t.getAmount(),
                    t.getType(),
                    ""
                ));
            }
        }

        return responses;
    }

    public List<TransactionHistoryResponse> getTransactionHistory(Long account, Long holder) {
        Account currentAccount = accountRepository.findById(account).orElseThrow(() -> new ResourceNotFoundException("No account found"));

        if (currentAccount.getHolder().getHolderId() != holder) {
            throw new AccountAccessDeniedException("You don't have access to this account");
        }

        List<Transaction> transactions = transactionRepository.findByAccount(currentAccount);

        List<TransactionHistoryResponse> responses = new ArrayList<TransactionHistoryResponse>();

        for (Transaction t : transactions) {


            if (t.getType() == Transaction.Type.TRANSFER_IN || t.getType() == Transaction.Type.TRANSFER_OUT) {
                responses.add(new TransactionHistoryResponse(
                    t.getTransactionId(),
                    t.getTimeStamp(),
                    t.getAmount(),
                    t.getType(),
                    t.getRelatedAccount().getAccountNum()
                ));
            } else {
                responses.add(new TransactionHistoryResponse(
                    t.getTransactionId(),
                    t.getTimeStamp(),
                    t.getAmount(),
                    t.getType(),
                    ""
                ));
            }
        }

        return responses;
    }
}
