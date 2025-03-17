package com.example.foodordering.service;

import com.example.foodordering.dto.request.AccountCreationRequest;
import com.example.foodordering.entity.Account;
import com.example.foodordering.repository.AccountRepositorry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountRepositorry accountRepositorry;

    public Account createAccouunt(AccountCreationRequest request) {
        Account account = new Account();
        if (accountRepositorry.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        AccountCreationRequest request1 = AccountCreationRequest.builder()
                                            .username("Warwick John")
                                            .password("MyWife")
                                            .build();
        account.setUsername(request.getUsername());
        account.setPassword(request.getPassword());

        return account;
    }
}
