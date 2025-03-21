package com.example.foodordering.service;

import com.example.foodordering.dto.request.LoginRequest;
import com.example.foodordering.dto.request.PasswordResetRequest;
import com.example.foodordering.entity.Account;
import com.example.foodordering.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testLogin_Fail_UserNotFound() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("nonexistentuser");
        loginRequest.setPassword("password123");

        when(accountRepository.findByUsername("nonexistentuser")).thenReturn(Optional.empty());

        String result = authService.login(loginRequest);

        assertEquals("Tài khoản không tồn tại!", result);
    }

    @Test
    void testLogin_Success() {
        Account account = new Account();
        account.setUsername("testuser");
        account.setPassword("password123"); // Không mã hóa

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");

        when(accountRepository.findByUsername("testuser")).thenReturn(Optional.of(account));

        String result = authService.login(loginRequest);

        assertEquals("Đăng nhập thành công!", result);
    }

    @Test
    void testResetPassword_Success() {
        Account account = new Account();
        account.setEmail("test@example.com");

        PasswordResetRequest resetRequest = new PasswordResetRequest();
        resetRequest.setEmail("test@example.com");
        resetRequest.setNewPassword("newpassword123"); // Không mã hóa

        when(accountRepository.findByEmail("test@example.com")).thenReturn(Optional.of(account));

        String result = authService.resetPassword(resetRequest);

        assertEquals("Mật khẩu đã được đặt lại thành công!", result);
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void testResetPassword_Fail_EmailNotFound() {
        PasswordResetRequest resetRequest = new PasswordResetRequest();
        resetRequest.setEmail("nonexistent@example.com");
        resetRequest.setNewPassword("newpassword123");

        when(accountRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        String result = authService.resetPassword(resetRequest);

        assertEquals("Email không tồn tại!", result);
    }
}
