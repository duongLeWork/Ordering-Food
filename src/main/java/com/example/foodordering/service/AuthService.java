package com.example.foodordering.service;

import com.example.foodordering.dto.request.LoginRequest;
import com.example.foodordering.dto.request.PasswordResetRequest;
import com.example.foodordering.entity.Account;
import com.example.foodordering.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private AccountRepository accountRepository;

    /**
     * Xác thực đăng nhập (Không mã hóa mật khẩu)
     */
    public String login(LoginRequest request) {
        Optional<Account> accountOpt = accountRepository.findByUsername(request.getUsername());

        if (accountOpt.isEmpty()) {
            return "Tài khoản không tồn tại!";
        }

        Account account = accountOpt.get();

        if (!request.getPassword().equals(account.getPassword())) {
            return "Mật khẩu không đúng!";
        }

        return "Đăng nhập thành công!";
    }

    /**
     * Đặt lại mật khẩu (Không mã hóa mật khẩu)
     */
    public String resetPassword(PasswordResetRequest request) {
        Optional<Account> accountOpt = accountRepository.findByEmail(request.getEmail());

        if (accountOpt.isEmpty()) {
            return "Email không tồn tại!";
        }

        Account account = accountOpt.get();
        account.setPassword(request.getNewPassword()); // Lưu mật khẩu dạng plaintext
        accountRepository.save(account);

        return "Mật khẩu đã được đặt lại thành công!";
    }
}
