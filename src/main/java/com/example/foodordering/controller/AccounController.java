package com.example.foodordering.controller;

import com.example.foodordering.dto.request.AccountCreationRequest;
import com.example.foodordering.service.AccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class AccounController {
    AccountService accountService;


    @PostMapping("")
    public void createAccount(@RequestBody AccountCreationRequest request) {


    }

}
