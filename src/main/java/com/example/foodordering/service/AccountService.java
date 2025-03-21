package com.example.foodordering.service;

import com.example.foodordering.dto.request.AccountCreationRequest;
import com.example.foodordering.dto.request.AccountUpdateRequest;
import com.example.foodordering.dto.response.AccountResponse;
import com.example.foodordering.entity.Account;
import com.example.foodordering.exception.AppException;
import com.example.foodordering.exception.ErrorCode;
import com.example.foodordering.mapper.AccountMapper;
import com.example.foodordering.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountMapper accountMapper;


    /**
     * Creates a new account.
     *
     * @param request the account creation request containing user details.
     * @return the created account as an {@link AccountResponse}.
     * @throws AppException if the username already exists.
     */
    public AccountResponse createAccount(AccountCreationRequest request) {
        if (accountRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        Account account = accountMapper.toAccount(request);
        accountRepository.save(account);
        return accountMapper.toAccountResponse(account);
    }
    /**
     * Updates an existing account.
     *
     * @param accountId the ID of the account to update.
     * @param request the account update request containing updated details.
     * @return the updated account as an {@link AccountResponse}.
     * @throws AppException if the account does not exist.
     */
    public AccountResponse updateAccount(Long accountId, AccountUpdateRequest request) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED));
        accountMapper.updateAccount(account, request);
        accountRepository.save(account);
        return accountMapper.toAccountResponse(account);
    }

    /**
     * Retrieves an account by its ID.
     *
     * @param accountId the ID of the account to retrieve.
     * @return the retrieved account as an {@link AccountResponse}.
     * @throws RuntimeException if the account is not found.
     */
    public AccountResponse getAccount(Long accountId) {
        return accountMapper.toAccountResponse(accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found")));
    }
    /**
     * Deletes an account by its ID.
     *
     * @param accountId the ID of the account to delete.
     */
    public void deleteAccount(Long accountId) {
        accountRepository.deleteById(accountId);
    }
    /**
     * Retrieves all accounts.
     *
     * @return a list of all accounts as {@link AccountResponse}.
     */
    public List<AccountResponse> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        List<AccountResponse> accountResponses = new ArrayList<>();
        accounts.forEach(account -> accountResponses.add(accountMapper.toAccountResponse(account)));
        return accountResponses;
    }
}
