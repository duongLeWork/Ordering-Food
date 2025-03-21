package com.example.foodordering.repository;

import com.example.foodordering.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    boolean existsByUsername(String username);
    Optional<Account> findByUsername(String username);
    Optional<Account> findByEmail(String email);
}
