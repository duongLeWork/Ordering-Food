package com.example.foodordering.service;

import com.example.foodordering.dto.request.AccountCreationRequest;
import com.example.foodordering.dto.request.AccountUpdateRequest;
import com.example.foodordering.entity.Account;
import com.example.foodordering.exception.AppException;
import com.example.foodordering.exception.ErrorCode;
import com.example.foodordering.mapper.AccountMapper;
import com.example.foodordering.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountMapper accountMapper;

    public Account createAccount(AccountCreationRequest request) {

        if (accountRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        Account account = accountMapper.toAccount(request);

        return accountRepository.save(account);
    }

    public Account updateAccount(Long accountId, AccountUpdateRequest request) {
        Account account = getAccount(accountId);

        accountMapper.updateAccount(account, request);

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
