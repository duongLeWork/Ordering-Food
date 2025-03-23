package com.example.foodordering.controller;

import com.example.foodordering.dto.request.AccountUpdateRequest;
import com.example.foodordering.dto.response.AccountResponse;
import com.example.foodordering.dto.response.ApiResponse;
import com.example.foodordering.service.AccountManagementService;
import com.example.foodordering.service.AccountRegistrationService;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@RequestMapping("/manager/management")
public class AccountManagementController {

    @Autowired
    private AccountManagementService accountManagementService;

    @PutMapping("{id}")
    public ApiResponse<AccountResponse> updateAccount(@PathVariable Long id,
                                                      @RequestBody AccountUpdateRequest request) {
        ApiResponse<AccountResponse> response = new ApiResponse<>();

        AccountResponse accountResponse = accountManagementService.updateAccount(id, request);

        response.setCode(1000);
        response.setMessage("Success");
        response.setData(accountResponse);

        return response;

    }

    @DeleteMapping("{id}")
    public void deleteAccount(@PathVariable Long id) {
        accountManagementService.deleteAccount(id);
    }

    @GetMapping("")
    public ApiResponse<List<AccountResponse>> getAllAccounts() {
        ApiResponse<List<AccountResponse>> response = new ApiResponse<>();

        List<AccountResponse> accountResponse = accountManagementService.getAllAccounts();

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
        response.setData(accountManagementService.getAccount(accountId));

        return response;
    }

    //    @GetMapping("/csrf-token")
//    public CsrfToken getCsrfToken(HttpServletRequest request) {
//        return (CsrfToken) request.getAttribute("_csrf");
//    }
    @GetMapping("/tuu")
    public String greet(HttpServletRequest request) {
        return "Token: "+request.getSession().getId();
    }
}
