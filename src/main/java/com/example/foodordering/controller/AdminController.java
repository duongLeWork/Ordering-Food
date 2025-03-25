package com.example.foodordering.controller;

import com.example.foodordering.models.Category;
import com.example.foodordering.models.PaymentMethod;
import com.example.foodordering.models.Customer;
import com.example.foodordering.models.Order.FoodOrder;
import com.example.foodordering.models.Order.OrderMenu;
import com.example.foodordering.models.Order.OrderStatus;
import com.example.foodordering.models.Product;
import com.example.foodordering.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @GetMapping("/product-list")
    public String productListPage(Model model) {
        // Sample category data
        Category pizzaCategory = new Category(1, "Pizza", "Additional Price");
        Category drinkCategory = new Category(2, "Drink", "Specific Price");
        Category pastaCategory = new Category(3, "Pasta", "Specific Price");

        // Sample product data with associated categories
        List<Product> products = Arrays.asList(
                new Product(1, "Pepperoni Pizza", 5.00, 30, "/images/pizza.jpg", true, pizzaCategory),
                new Product(2, "Cheese Pizza", 4.50, 25, "/images/cheese-pizza.jpg", true, pizzaCategory),
                new Product(3, "Veggie Pizza", 4.00, 20, "/images/veggie-pizza.jpg", false, pizzaCategory)
        );

        // Example filters list
        List<String> filters = Arrays.asList("Price Range", "Category Type", "Availability");

        model.addAttribute("filters", filters);
        model.addAttribute("products", products);
        model.addAttribute("pageTitle", "Product List");
        model.addAttribute("pagePath", "Master Data / Product / Product List");
        model.addAttribute("activePage", "productList");

        return "adminPage/index";  // This corresponds to the Thymeleaf template (product-list.html)
    }
    @GetMapping("/categories")
    public String categoriesPage(Model model) {
        // Sample categories data
        List<Category> categories = Arrays.asList(
                new Category(1, "Pizza", "Additional Price"),
                new Category(2, "Drink", "Specific Price"),
                new Category(3, "Pasta", "Specific Price")
        );
        model.addAttribute("categories", categories);
        model.addAttribute("pageTitle", "Categories");
        model.addAttribute("activePage", "categories");
        model.addAttribute("pagePath", "Master Data / Product / Categories");
        model.addAttribute("filters", Arrays.asList("Price Range", "Category Type", "Availability"));
        return "adminPage/index";
    }

    @GetMapping("/settings/user-account")
    public String usersAccountPage(Model model) {
        List<User> users = Arrays.asList(
                new User("Amalia Aristiningsih", "amaliaaris@mail.com", "Administrator", "images/avatar.jpg"),
                new User("John Doe", "johndoe@mail.com", "Crew Store", "images/avatar.jpg"),
                new User("Jane Smith", "janesmith@mail.com", "Crew Store", "images/avatar.jpg")
        );
        model.addAttribute("users", users);
        model.addAttribute("pageTitle", "Settings");
        model.addAttribute("activePage", "userAccount");
        model.addAttribute("pagePath", "Master Data / Settings / Users Account");

        // Redirect to the settings page with activePage = userAccount
        return "adminPage/index";
    }

    @GetMapping("/settings/payment-method")
    public String showPaymentMethods(Model model) {
        // Example data - you would normally fetch this from a database
        List<PaymentMethod> paymentMethods = Arrays.asList(
                new PaymentMethod("Credit or Debit Card", "Sent automatically to the customer after they place their order.", true),
                new PaymentMethod("Paypal", "Sent automatically to the customer after they place their order.", true),
                new PaymentMethod("Cash on Delivery (COD)", "Sent automatically to the customer after they place their order.", false)
        );

        // Add the payment methods to the model
        model.addAttribute("paymentMethods", paymentMethods);
        model.addAttribute("pageTitle", "Settings");
        model.addAttribute("activePage", "paymentMethod");
        model.addAttribute("pagePath", "Master Data / Settings / Payment Method");

        // Redirect to the settings page with activePage = paymentMethod
        return "adminPage/index";
    }
    @GetMapping("/dashboard")
    public String dashboardPage(Model model) {
        //data go here

        model.addAttribute("pageTitle", "Dashboard");
        model.addAttribute("pagePath", "Main Menu / Dashboard");
        model.addAttribute("activePage", "dashboard");
        return "adminPage/index";
    }
    @GetMapping("/orders")
    public String showOrderPage(Model model) {
        // Create sample customers
        Customer customer1 = new Customer(1L, "Devon Lane");
        Customer customer2 = new Customer(2L, "Jane Smith");
        Customer customer3 = new Customer(3L, "John Doe");

        // Create sample order statuses
        OrderStatus newOrderStatus = new OrderStatus(1L, "New Order");
        OrderStatus processingStatus = new OrderStatus(2L, "Processing");
        OrderStatus canceledStatus = new OrderStatus(3L, "Canceled");

        // Create sample order menus
        OrderMenu pizzaMenu = new OrderMenu(1L, "Pizza");
        OrderMenu burgerMenu = new OrderMenu(2L, "Burger");
        OrderMenu saladMenu = new OrderMenu(3L, "Salad");

        // Create sample food orders
        List<FoodOrder> orders = Arrays.asList(
                new FoodOrder(1L, customer1, newOrderStatus, pizzaMenu, new BigDecimal(250), new BigDecimal(250)),
                new FoodOrder(2L, customer2, processingStatus, burgerMenu, new BigDecimal(500), new BigDecimal(500)),
                new FoodOrder(3L, customer3, canceledStatus, saladMenu, new BigDecimal(250), new BigDecimal(250))
        );
        model.addAttribute("orders", orders);
        model.addAttribute("pageTitle", "Orders");
        model.addAttribute("pagePath", "Main Menu / Orders");
        model.addAttribute("activePage", "orders");
        return "adminPage/index";
    }
    @GetMapping("/customers")
    public String CustomerPage(Model model) {
        // Create sample customer data
        List<Customer> customers = Arrays.asList(
                new Customer(1L, "John Santos", "(012) 345-6789", "john-santos@mail.om", new BigDecimal(1290), 120),
                new Customer(2L, "Jane Doe", "(013) 345-6789", "jane.doe@mail.om", new BigDecimal(850), 90),
                new Customer(3L, "Alex Smith", "(014) 345-6789", "alex.smith@mail.om", new BigDecimal(2200), 150)
        );

        model.addAttribute("customers", customers);
        model.addAttribute("pageTitle", "Customers");
        model.addAttribute("pagePath", "Main Menu / Customers");
        model.addAttribute("activePage", "customers");

        return "adminPage/index";
    }

}
