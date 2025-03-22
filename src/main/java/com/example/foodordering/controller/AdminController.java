package com.example.foodordering.controller;

import com.example.foodordering.models.Category;
import com.example.foodordering.models.Product;
import com.example.foodordering.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class AdminController {
    @GetMapping("/product-list")
    public String productListPage(Model model) {
        List<Product> products = Arrays.asList(
                new Product(1, "Pepperoni Pizza", 5.00, 30, "images/pizza.jpg", true),
                new Product(2, "Cheese Pizza", 4.50, 25, "images/cheese-pizza.jpg", true),
                new Product(3, "Veggie Pizza", 4.00, 20, "images/veggie-pizza.jpg", false)
        );

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

    @GetMapping("/settings")
    public String settingsPage(Model model) {
        // Sample users data
        List<User> users = Arrays.asList(
                new User("Amalia Aristiningsih", "amaliaaris@mail.com", "Administrator", "images/avatar.jpg"),
                new User("John Doe", "johndoe@mail.com", "Crew Store", "images/avatar.jpg"),
                new User("Jane Smith", "janesmith@mail.com", "Crew Store", "images/avatar.jpg")
        );

        model.addAttribute("users", users);
        model.addAttribute("pageTitle", "Users Account");
        model.addAttribute("pagePath", "Master Data / Setting / Users Account");
        model.addAttribute("activePage", "settings");  // Set the activePage to 'settings'
        return "adminPage/index";  // This will render the index.html with the settings fragment
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
    public String OrderPage(Model model) {
        //data go here

        model.addAttribute("pageTitle", "Orders");
        model.addAttribute("pagePath", "Main Menu / Orders");
        model.addAttribute("activePage", "orders");
        return "adminPage/index";
    }
    @GetMapping("/customers")
    public String CustomerPage(Model model) {
        //data go here

        model.addAttribute("pageTitle", "Customers");
        model.addAttribute("pagePath", "Main Menu / Customers");
        model.addAttribute("activePage", "customers");
        return "adminPage/index";
    }

}
