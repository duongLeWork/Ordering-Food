package com.example.foodordering.controller.admin;

import com.example.foodordering.entity.*;
import com.example.foodordering.repository.OrderMenuItemRepository;
import com.example.foodordering.repository.OrderStatusRepository;
import com.example.foodordering.repository.FoodOrderRepository;
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
import java.math.BigDecimal;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private FoodOrderRepository foodOrderRepository;
    @Autowired
    private OrderStatusRepository orderStatusRepository;
    @Autowired
    private OrderMenuItemRepository orderMenuItemRepository;
    @Autowired
    private AccountRepository accountRepository;


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

    @GetMapping("/customers")
    public String customerPage(Model model,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "5") int rowsPerPage,
                               @RequestParam(defaultValue = "ASC") String sortOrder,
                               @RequestParam(required = false) String searchTerm) {
        Pageable pageable = PageRequest.of(page - 1, rowsPerPage);
        Page<Customer> customersPage;

        // Apply sorting based on 'sortOrder'
        if (sortOrder.equalsIgnoreCase("ASC")) {
            customersPage = customerRepository.findCustomersSortedByTotalSpentAsc(pageable);
        } else {
            customersPage = customerRepository.findCustomersSortedByTotalSpentDesc(pageable);
        }

        // If a search term is provided, filter customers based on firstname or lastname
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            customersPage = customerRepository.findByFirstnameContainingOrLastnameContaining(searchTerm, pageable);
        }

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

        // Calculate totalItems and totalSpent for each customer after fetching from DB
        customersPage.getContent().forEach(Customer::calculateTotals);

        return "adminPage/index"; // This will map to admin/customers.html
    }

    @GetMapping("/orders")
    public String showOrderPage(Model model,
                         @RequestParam(defaultValue = "1") int page,
                         @RequestParam(defaultValue = "5") int rowsPerPage,
                         @RequestParam(required = false) String searchTerm) {
        // Pagination logic using Spring Data pagination
        Pageable pageable = PageRequest.of(page - 1, rowsPerPage);

        Page<FoodOrder> ordersPage;

        // Apply search if a search term is provided
        if (searchTerm != null && !searchTerm.isEmpty()) {
            ordersPage = foodOrderRepository.findByCustomerNameContainingIgnoreCase(searchTerm, pageable);
        } else {
            ordersPage = foodOrderRepository.findAll(pageable);  // If no search term, retrieve all orders
        }

        List<FoodOrder> orders = ordersPage.getContent();  // Get paged orders
        int totalPages = ordersPage.getTotalPages();  // Total pages
        long totalOrders = ordersPage.getTotalElements();  // Total orders

        // Calculate total items in each order and add it to the order
        orders.forEach(order -> {
            int totalItems = order.getOrderMenuItems().stream()
                    .mapToInt(item -> item.getQuantityOrdered())  // Sum quantities
                    .sum();
            order.setTotalItems(totalItems);  // Store the total items count in the order
        });

        // Pass attributes to the model
        model.addAttribute("orders", orders);
        model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("rowsPerPage", rowsPerPage);
        model.addAttribute("path", "/admin/orders");
        model.addAttribute("pageTitle", "Orders");
        model.addAttribute("pagePath", "Main Menu / Orders");
        model.addAttribute("activePage", "orders");
        return "adminPage/index"; // (optional)
    }
    @GetMapping("/orders/{id}")
    public String showOrderDetails(@PathVariable("id") int id, Model model) {
        FoodOrder order = foodOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        model.addAttribute("order", order);
        model.addAttribute("pageTitle", "Order Details");
        model.addAttribute("pagePath", "Main Menu / Order / Details");
        model.addAttribute("activePage", "orderDetail");// Add order to the model
        return "adminPage/index";  // Template for the detailed order view
    }

    @GetMapping("/dashboard")
    public String dashboardPage(Model model) {
        // Fetch real data from the database
        List<Customer> customers = customerRepository.findAll();
        List<FoodOrder> orders = foodOrderRepository.findAll();
        List<Food> foods = foodRepository.findAll();

        // Sort the foods by sales count (simulating top-selling products)
        // If you have an actual field for sales, replace this sorting logic
        foods.sort((f1, f2) -> Integer.compare(f2.getId(), f1.getId())); // Example for sorting by ID, update to suit your needs

        // Only get the top 4 selling foods (if applicable)
        List<Food> topSellingFoods = foods.subList(0, Math.min(4, foods.size()));

        // Simulate order status counts
        long newOrders = orders.stream().filter(order -> !order.isOrderStatus()).count(); // false = giỏ hàng
        long orderProcessed = orders.stream().filter(order -> order.isOrderStatus()).count(); // true = đã đặt hàng

        // Simulate new customers (e.g., for this month)
        long newCustomers = customers.size(); // Adjust as needed based on your business logic

        // Add all necessary attributes to the model
        model.addAttribute("newOrders", newOrders);
        model.addAttribute("orderProcessed", orderProcessed);
        model.addAttribute("newCustomers", newCustomers);
        model.addAttribute("topSellingFoods", topSellingFoods);
        model.addAttribute("orders", orders);
        model.addAttribute("customers", customers);
        model.addAttribute("pageTitle", "Dashboard");
        model.addAttribute("pagePath", "Main Menu / Dashboard");
        model.addAttribute("activePage", "dashboard");

        return "adminPage/index"; // This maps to the templates/admin/dashboard.html
    }

    // Add more mappings as needed
}
