package com.example.foodordering.service;

import com.example.foodordering.dto.request.AccountCreationRequest;
import com.example.foodordering.dto.request.AccountUpdateRequest;
import com.example.foodordering.entity.Account;
import com.example.foodordering.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account createAccount(AccountCreationRequest request) {
        Account account = new Account();

        if (accountRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        account.setUsername(request.getUsername());
        account.setPassword(request.getPassword());

        return accountRepository.save(account);
    }

    public Account updateAccount(Long accountId, AccountUpdateRequest request) {
        Account account = getAccount(accountId);

        account.setPassword(request.getPassword());
        account.setFirstName(request.getFirstName());
        account.setLastName(request.getLastName());

        return accountRepository.save(account);

    }

    public Account getAccount(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));

    }

    public void deleteAccount(Long accountId) {
        accountRepository.deleteById(accountId);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
}
