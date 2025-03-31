package com.example.foodordering.controller;

import com.example.foodordering.dto.request.AuthenticationRequest;
import com.example.foodordering.dto.response.AuthenticationResponse;
import com.example.foodordering.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("authenticationRequest", new AuthenticationRequest());
        return "login";
    }

    @PostMapping("/login")
    public String authenticate(@ModelAttribute AuthenticationRequest authenticationRequest, Model model) {
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(authenticationRequest);

        // You can handle the authentication response here, e.g., save it in session or redirect based on success/failure
        if (authenticationResponse != null) {
            // Successful authentication logic here
            return "redirect:/home"; // Example redirect after successful login
        } else {
            // Failed authentication logic here
            model.addAttribute("error", "Authentication failed");
            return "login"; // Return to login page with error message
        }
    }
}
