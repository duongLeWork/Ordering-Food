package com.example.foodordering.service;

import com.example.foodordering.dto.request.AccountUpdateRequest;
import com.example.foodordering.dto.response.AccountResponse;
import com.example.foodordering.entity.Account;
import com.example.foodordering.exception.AppException;
import com.example.foodordering.exception.ErrorCode;
import com.example.foodordering.mapper.AccountMapper;
import com.example.foodordering.mapper.AccountResponseMapper;
import com.example.foodordering.mapper.CustomerMapper;
import com.example.foodordering.repository.intf.AccountRepository;
import com.example.foodordering.repository.intf.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountManagementService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    AccountResponseMapper accountResponseMapper;

    @Autowired
    CustomerMapper customerMapper;

    @Autowired
    private CustomerRepository customerRepository;

    /**
     * Updates an existing account.
     *
     * @param accountId the ID of the account to update.
     * @param request   the account update request containing updated details.
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
//        return accountMapper.toAccountResponse(accountRepository.findById(accountId)
//                .orElseThrow(() -> new RuntimeException("Account not found")));
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED));


        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setFirstname(account.getCustomer().getFirstname());
        accountResponse.setLastname(account.getCustomer().getLastname());
        accountResponse.setPhoneNumber(account.getCustomer().getPhoneNumber());

        return accountResponse;
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
