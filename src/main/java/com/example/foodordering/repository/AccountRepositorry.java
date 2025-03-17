package com.example.foodordering.repository;

import com.example.foodordering.entity.Account;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepositorry {

    boolean existsByUsername(String username);

    Optional<Account> findByUsername(String username);
}
