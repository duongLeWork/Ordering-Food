package com.example.foodordering.controller;

import com.example.foodordering.config.CustomUserDetails;
import com.example.foodordering.dto.request.SearchFoodRequest;
import com.example.foodordering.dto.response.FoodResponse;
import com.example.foodordering.entity.Food;
import com.example.foodordering.service.GuestService;
import com.example.foodordering.utils.UserDetailsHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller xử lý các yêu cầu liên quan đến khách, bao gồm hiển thị danh sách món ăn,
 * tìm kiếm và xem chi tiết món ăn.
 */
@Controller
@RequestMapping("/")
public class GuestController {

    private final GuestService guestService;

    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    /**
     * Hiển thị trang chủ.
     *
     * @return tên view của trang chủ.
     */

    @GetMapping
    public String getRoot(Model model)
    {
        return "redirect:/home";

    }

    @GetMapping("/home")
    public String homepage(Model model) {
        Optional<CustomUserDetails> userDetails = UserDetailsHelper.getUserDetails();
        if (userDetails.isPresent()) {
            // Truyền thông tin username vào model để hiển thị trên home.html
            model.addAttribute("username", userDetails.get().getUsername());
            model.addAttribute("password", userDetails.get().getPassword());
            return "home"; // Trả về home.html
        }
        // Nếu chưa đăng nhập, chuyển hướng về trang login
        return "redirect:/login";
    }
    /**
     * Lấy danh sách tất cả các món ăn có sẵn và hiển thị trên trang chủ.
     *
     * @param model đối tượng Model để truyền dữ liệu tới view.
     * @return tên view hiển thị danh sách món ăn.
     */
    @GetMapping("/dishes")
    public String getAvailableDishes(Model model) {
        List<Food> dishes = guestService.getAvailableDishes();
        model.addAttribute("dishes", dishes);
        return "dish/list";
    }

    /**
     * Tìm kiếm các món ăn dựa trên từ khóa, sắp xếp, danh mục và số lượng kết quả tối đa.
     *
     * @param keyword    từ khóa tìm kiếm (tùy chọn).
     * @param sortBy     tiêu chí sắp xếp (tùy chọn).
     * @param model      đối tượng Model để truyền dữ liệu tới view.
     * @return tên view hiển thị kết quả tìm kiếm.
     */
    @GetMapping("/search")
    public String searchDishes(@RequestParam(required = false) String keyword,
                               @RequestParam(required = false) String sortBy,
                               Model model) {
        SearchFoodRequest request = new SearchFoodRequest(keyword, sortBy);
        List<FoodResponse> results = guestService.searchDishes(request).getData();
        model.addAttribute("dishes", results);
        return "search";
    }

    /**
     * Lấy thông tin chi tiết của một món ăn cụ thể.
     *
     * @param foodId ID của món ăn.
     * @param model  đối tượng Model để truyền dữ liệu tới view.
     * @return tên view hiển thị chi tiết món ăn.
     */
    @GetMapping("/dish/{foodId}")
    public String getFoodDetails(@PathVariable int foodId, Model model) {
        Food response = guestService.getFoodDetails(foodId);
        if (response != null) {
            model.addAttribute("food", response);
            Optional<CustomUserDetails> userDetails = UserDetailsHelper.getUserDetails();
            userDetails.ifPresent(details -> model.addAttribute("userDetails", details));
            return "dish/details";
        } else {
            model.addAttribute("foodNotFound", true);
            return "dish/details";
        }
    }
}
