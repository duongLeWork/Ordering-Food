package com.example.foodordering.controller;

import com.example.foodordering.dto.request.AuthenticationRequest;
import com.example.foodordering.dto.response.AuthenticationResponse;
import com.example.foodordering.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

//    @PostMapping("/login")
//    public String authenticate(@ModelAttribute AuthenticationRequest authenticationRequest, Model model) {
//        try {
//            UsernamePasswordAuthenticationToken authenticationToken = authenticationService.authenticate(authenticationRequest);
//
//            if (authenticationToken.isAuthenticated()) {
//                return "redirect:/home";
//            }
//        } catch (Exception e) {
//            model.addAttribute("error", "Authentication failed: " + e.getMessage());
//        }
//        return "login";
//    }

}
