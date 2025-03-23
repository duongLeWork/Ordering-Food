package com.example.foodordering.controller;

import com.example.foodordering.dto.request.AuthenticationRequest;
import com.example.foodordering.dto.response.ApiResponse;
import com.example.foodordering.dto.response.AuthenticationResponse;
import com.example.foodordering.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> authenicate(@RequestBody AuthenticationRequest authenticationRequest) {
        ApiResponse<AuthenticationResponse> response = new ApiResponse<>();
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(authenticationRequest);
        response.setData(authenticationResponse);
        response.setCode(200);
        response.setMessage("Authentication Successful");
        return response;
    }
}
