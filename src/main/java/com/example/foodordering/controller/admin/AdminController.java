package com.example.foodordering.controller.admin;

import com.example.foodordering.entity.Account;
import com.example.foodordering.entity.Customer;
import com.example.foodordering.entity.Food;
import com.example.foodordering.repository.FoodRepository;
import com.example.foodordering.repository.intf.AccountRepository;
import com.example.foodordering.repository.intf.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private FoodRepository foodRepository;

    @GetMapping("/product-list")
    public String productsListPage(Model model,
                                   @RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "5") int rowsPerPage,

                                   @RequestParam(required = false) Boolean available,
                                   @RequestParam(required = false) String search) {
        // Pageable uses 0-based index
        Pageable pageable = PageRequest.of(page - 1, rowsPerPage);
        Page<Food> foodPage;

        if (search != null && !search.isEmpty() && available != null) {
            // Search for both name and availability
            foodPage = foodRepository.findByNameContainingIgnoreCaseAndIsAvailable(search, available, pageable);
        } else if (search != null && !search.isEmpty()) {
            // Search only by name
            foodPage = foodRepository.findByNameContainingIgnoreCase(search, pageable);
        } else if (available != null) {
            // Filter only by availability
            foodPage = foodRepository.findByIsAvailable(available, pageable);
        } else {
            // No filters, return all products
            foodPage = foodRepository.findAll(pageable);
        }

        List<Food> pagedFoods = foodPage.getContent();
        List<String> filters = List.of("Availability");

        int totalPages = foodPage.getTotalPages();
        model.addAttribute("products", pagedFoods);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("rowsPerPage", rowsPerPage);
        model.addAttribute("filters", filters);
        model.addAttribute("search", search);
        model.addAttribute("available", available);
        model.addAttribute("path", "/admin/product-list");
        model.addAttribute("pageTitle", "Product List");
        model.addAttribute("pagePath", "Master Data / Product");
        model.addAttribute("activePage", "productList");
        return "adminPage/index"; // maps to templates/admin/products.html
    }

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/account")
    public String accountsPage(Model model,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "5") int rowsPerPage,
                               @RequestParam(required = false) String search,
                               @RequestParam(required = false) String role) {
        // Pageable uses 0-based index
        Pageable pageable = PageRequest.of(page - 1, rowsPerPage);
        Page<Account> accountPage;

        // Use repository with custom filtering
        if (search != null && !search.isBlank()) {
            // Search accounts by username (case-insensitive)
            accountPage = accountRepository.findByUsernameContainingIgnoreCase(search, pageable);
        } else {
            // Get all accounts if no filters are applied
            accountPage = accountRepository.findAll(pageable);
        }

        // Get the content of the current page
        List<Account> accounts = accountPage.getContent();

        // Calculate total pages for pagination
        int totalPages = accountPage.getTotalPages();

        model.addAttribute("accounts", accounts);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("rowsPerPage", rowsPerPage);
        model.addAttribute("search", search);
        model.addAttribute("pageTitle", "Users");
        model.addAttribute("path", "/admin/account");
        model.addAttribute("activePage", "userAccount");
        model.addAttribute("pagePath", "Master Data / Users");

        return "adminPage/index";
    }
    @GetMapping("/account/{accountId}")
    public String userDetailPage(@PathVariable Long accountId, Model model) {
        // Find the Account by the accountId
        Account account = accountRepository.findByAccountId(accountId);

        if (account == null) {
            model.addAttribute("error", "User not found.");
            return "error";  // Return to an error page or display a message if not found
        }

        // Add the Account details to the model
        model.addAttribute("accounts", account);
        model.addAttribute("pageTitle", "User Detail");
        model.addAttribute("activePage", "userDetail");
        model.addAttribute("pagePath", "Master Data / User / Detail");

        return "adminPage/index";  // Return to the user detail page
    }


    @Autowired
    private CustomerRepository customerRepository; // Inject the customer repository

    @GetMapping("/customers")
    public String customerPage(Model model,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "5") int rowsPerPage,
                               @RequestParam(defaultValue = "ASC") String sortOrder,
                               @RequestParam(required = false) String searchTerm) {
        Pageable pageable = PageRequest.of(page - 1, rowsPerPage);

        // Apply sorting based on 'sortOrder'
        if (sortOrder.equalsIgnoreCase("ASC")) {
            pageable = PageRequest.of(page - 1, rowsPerPage, Sort.by(Sort.Order.asc("totalSpent")));
        } else {
            pageable = PageRequest.of(page - 1, rowsPerPage, Sort.by(Sort.Order.desc("totalSpent")));
        }

        Page<Customer> customersPage;
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            customersPage = customerRepository.findByFirstnameContainingOrLastnameContaining(searchTerm, pageable);
        } else {
            // If no search term, fetch all customers
            customersPage = customerRepository.findAllCustomers(pageable);
        }

        // Calculate totalSpent and orderCount for each customer if they aren't in the model yet
        customersPage.getContent().forEach(customer -> {
            //...
        });

        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("customers", customersPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", customersPage.getTotalPages());
        model.addAttribute("rowsPerPage", rowsPerPage);
        model.addAttribute("pageTitle", "Customers");
        model.addAttribute("pagePath", "Main Menu / Customers");
        model.addAttribute("path", "/admin/customers");
        model.addAttribute("activePage", "customers");

        return "adminPage/index"; // This will map to admin/customers.html
    }


    @GetMapping("/orders")
    public String orders() {
        return "admin/orders"; // (optional)
    }

    // Add more mappings as needed
}
