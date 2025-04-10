package com.example.foodordering.repository.intf;

import com.example.foodordering.entity.Customer;
import com.example.foodordering.entity.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findByPhoneNumber(String phoneNumber);

    @Query("SELECT c FROM Customer c WHERE c.account.role = 'CUSTOMER'")
    Page<Customer> findAllCustomers(Pageable pageable);

    @Query("SELECT c FROM Customer c WHERE (c.account.role = 'CUSTOMER') AND (c.firstname LIKE %?1% OR c.lastname LIKE %?1%)")
    Page<Customer> findByFirstnameContainingOrLastnameContaining(String searchTerm, Pageable pageable);

}
