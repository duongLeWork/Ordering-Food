package com.example.foodordering.service;

import com.example.foodordering.dto.request.AuthenticationRequest;
import com.example.foodordering.dto.response.AuthenticationResponse;
import com.example.foodordering.entity.Account;
import com.example.foodordering.repository.intf.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    AccountRepository accountRepository;

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        // Chưa thực sự xử lý (Mới chỉ dừng lại ở so sánh PW).
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        Account account = accountRepository.findByUsername(authenticationRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Account Not Found"));

        boolean authenticated = passwordEncoder.matches(authenticationRequest.getPassword(), account.getPassword());
        if (!authenticated) throw new RuntimeException("Invalid Password");

        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setAuthenticated(authenticated);
        return authenticationResponse;
    }
}
