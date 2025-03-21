package com.example.foodordering.controller;

import com.example.foodordering.dto.request.LoginRequest;
import com.example.foodordering.dto.request.PasswordResetRequest;
import com.example.foodordering.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody PasswordResetRequest request) {
        return authService.resetPassword(request);
    }
}
