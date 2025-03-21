package com.example.foodordering.controller;

import com.example.foodordering.dto.request.AccountCreationRequest;
import com.example.foodordering.dto.request.AccountUpdateRequest;
import com.example.foodordering.dto.response.ApiResponse;
import com.example.foodordering.dto.response.AccountResponse;
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
    public ApiResponse<AccountResponse> createAccount(@RequestBody @Valid AccountCreationRequest request) {
        ApiResponse<AccountResponse> response = new ApiResponse<>();
        AccountResponse accountResponse = accountService.createAccount(request);

        response.setData(accountResponse);
        return response;
    }

    @PutMapping("{id}")
    public ApiResponse<AccountResponse> updateAccount(@PathVariable Long id,
                                                      @RequestBody AccountUpdateRequest request) {
        ApiResponse<AccountResponse> response = new ApiResponse<>();

        AccountResponse accountResponse = accountService.updateAccount(id, request);

        response.setCode(1000);
        response.setMessage("Success");
        response.setData(accountResponse);

        return response;

    }

    @DeleteMapping("{id}")
    public void deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
    }

    @GetMapping("")
    public ApiResponse<List<AccountResponse>> getAllAccounts() {
        ApiResponse<List<AccountResponse>> response = new ApiResponse<>();
        List<AccountResponse> accountResponse = accountService.getAllAccounts();
        response.setCode(1000);
        response.setMessage("Success");
        response.setData(accountResponse);

        return response;
    }

    @GetMapping("{accountId}")
    public ApiResponse<AccountResponse> getAccount(@PathVariable Long accountId) {
        ApiResponse<AccountResponse> response = new ApiResponse<>();
        response.setCode(1000);
        response.setMessage("Success");
        response.setData(accountService.getAccount(accountId));

        return response;
    }
}
