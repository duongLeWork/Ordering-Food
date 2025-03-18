package com.example.foodordering.controller;

import com.example.foodordering.dto.request.AccountCreationRequest;
import com.example.foodordering.dto.request.AccountUpdateRequest;
import com.example.foodordering.dto.request.ApiResponse;
import com.example.foodordering.entity.Account;
import com.example.foodordering.service.AccountService;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccounController {

    @Autowired
    private AccountService accountService;


    @PostMapping("/users")
    public ApiResponse<Account> createAccount(@RequestBody @Valid AccountCreationRequest request) {
        ApiResponse<Account> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("Success");
        response.setResults(accountService.createAccount(request));

        return response;
    }

    @PutMapping("{id}")
    public Account updateAccount(@PathVariable Long id,
                                 @RequestBody AccountUpdateRequest request) {
        return accountService.updateAccount(id, request);

    }
    @DeleteMapping("{id}")
    public String deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return "Account deleted";
    }

    @GetMapping("")
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("{accountId}")
    public Account getAccount(@PathVariable Long accountId) {
        return accountService.getAccount(accountId);
    }
}
