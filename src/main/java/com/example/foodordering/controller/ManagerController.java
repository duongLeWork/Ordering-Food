package com.example.foodordering.controller;

import com.example.foodordering.dto.response.AccountResponse;
import com.example.foodordering.dto.response.ApiResponse;
import com.example.foodordering.entity.Customer;
import com.example.foodordering.entity.Food;
import com.example.foodordering.entity.FoodOrder;
import com.example.foodordering.service.ManagerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing administrative operations such as users, orders, and food items.
 */
@Controller
@RequestMapping("/manager")
public class ManagerController {

    private final ManagerService managerService;

    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    /**
     * Retrieves a list of all registered users.
     *
     * @param model Model to add user data.
     * @return Thymeleaf template for user list.
     */
    @GetMapping("/users")
    public String getAllUsers(Model model) {
        List<Customer> users = managerService.getAllUsers().getData();
        model.addAttribute("users", users);
        return "manager/user-list";
    }

    /**
     * Retrieves a list of all placed orders.
     *
     * @param model Model to add order data.
     * @return Thymeleaf template for order list.
     */
    @GetMapping("/orders")
    public String getAllOrders(Model model) {
        List<FoodOrder> orders = managerService.getAllOrders().getData();
        model.addAttribute("orders", orders);
        return "manager/order-list";
    }

    /**
     * Displays the form for adding a new food item.
     *
     * @param model Model to add food data.
     * @return Thymeleaf template for adding food.
     */
    @GetMapping("/food/add")
    public String showAddFoodForm(Model model) {
        model.addAttribute("food", new Food());
        return "manager/food-form";
    }

    /**
     * Adds a new food item.
     *
     * @param food The Food entity to be added.
     * @return Redirect to food list page.
     */
    @PostMapping("/food")
    public String addFood(@ModelAttribute Food food) {
        managerService.addFood(food);
        return "redirect:/manager/food";
    }

    /**
     * Displays the form for editing an existing food item.
     *
     * @param foodId ID of the food item to update.
     * @param model  Model to add food data.
     * @return Thymeleaf template for editing food.
     */
    @GetMapping("/food/edit/{foodId}")
    public String showEditFoodForm(@PathVariable int foodId, Model model) {
        Food food = managerService.getFoodById(foodId).getData();
        model.addAttribute("food", food);
        return "manager/food-form";
    }

    /**
     * Updates an existing food item.
     *
     * @param foodId      The ID of the food item to update.
     * @param updatedFood The Food entity containing updated information.
     * @return Redirect to food list page.
     */
    @PostMapping("/food/{foodId}")
    public String updateFood(@PathVariable int foodId, @ModelAttribute Food updatedFood) {
        managerService.updateFood(foodId, updatedFood);
        return "redirect:/manager/food";
    }

    /**
     * Deletes a food item.
     *
     * @param foodId The ID of the food item to delete.
     * @return Redirect to food list page.
     */
    @GetMapping("/food/delete/{foodId}")
    public String deleteFood(@PathVariable int foodId) {
        managerService.deleteFood(foodId);
        return "redirect:/manager/food";
    }

//    @DeleteMapping("{id}")
//    public void deleteAccount(@PathVariable Long id) {
//        accountManagementService.deleteAccount(id);
//    }
//
//    @GetMapping("")
//    public ApiResponse<List<AccountResponse>> getAllAccounts() {
//        ApiResponse<List<AccountResponse>> response = new ApiResponse<>();
//
//        List<AccountResponse> accountResponse = accountManagementService.getAllAccounts();
//
//        response.setCode(1000);
//        response.setMessage("Success");
//        response.setData(accountResponse);
//
//        return response;
//    }
//

}
