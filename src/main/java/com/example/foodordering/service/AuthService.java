package com.example.foodordering.service;

import com.example.foodordering.dto.request.LoginRequest;
import com.example.foodordering.dto.request.PasswordResetRequest;
import com.example.foodordering.dto.response.ApiResponse;
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
     * Xác thực đăng nhập
     */
    public ApiResponse<Account> login(LoginRequest request) {
        ApiResponse<Account> response = new ApiResponse<>();

        Optional<Account> accountOpt = accountRepository.findByUsername(request.getUsername());
        if (accountOpt.isEmpty()) {
            response.setCode(1404);
            response.setMessage("Tài khoản không tồn tại!");
            response.setData(null);
            return response;
        }

        Account account = accountOpt.get();
        if (!request.getPassword().equals(account.getPassword())) { // Không mã hóa mật khẩu
            response.setCode(1401);
            response.setMessage("Mật khẩu không đúng!");
            response.setData(null);
            return response;
        }

        response.setCode(1000);
        response.setMessage("Đăng nhập thành công!");
        response.setData(account);
        return response;
    }

    /**
     * Đặt lại mật khẩu
     */
    public ApiResponse<String> resetPassword(PasswordResetRequest request) {
        ApiResponse<String> response = new ApiResponse<>();

        Optional<Account> accountOpt = accountRepository.findByEmail(request.getEmail());
        if (accountOpt.isEmpty()) {
            response.setCode(1404);
            response.setMessage("Email không tồn tại!");
            response.setData(null);
            return response;
        }

        Account account = accountOpt.get();
        account.setPassword(request.getNewPassword()); // Không mã hóa mật khẩu
        accountRepository.save(account);

        response.setCode(1200);
        response.setMessage("Mật khẩu đã được đặt lại thành công!");
        response.setData("Success");
        return response;
    }
}
