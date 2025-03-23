package com.example.foodordering.controller;

import com.example.foodordering.dto.request.AccountRegistrationRequest;
import com.example.foodordering.dto.response.AccountResponse;
import com.example.foodordering.dto.response.ApiResponse;
import com.example.foodordering.service.AccountRegistrationService;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@RequestMapping("/registers")
public class AccountRegisterController {

    @Autowired
    private AccountRegistrationService accountService;

    @PostMapping("")
    public ApiResponse<AccountResponse> createAccount(@RequestBody @Valid AccountRegistrationRequest request) {
        ApiResponse<AccountResponse> response = new ApiResponse<>();

        AccountResponse accountResponse = accountService.createAccount(request);

        response.setData(accountResponse);
        return response;
    }
}
