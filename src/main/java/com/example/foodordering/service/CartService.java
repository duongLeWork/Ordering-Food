package com.example.foodordering.service;

import com.example.foodordering.config.CustomUserDetails;
import com.example.foodordering.dto.request.CartItemRequest;
import com.example.foodordering.dto.response.ApiResponse;
import com.example.foodordering.entity.*;
import com.example.foodordering.repository.FoodOrderRepository;
import com.example.foodordering.repository.OrderStatusRepository;
import com.example.foodordering.repository.intf.CustomerRepository;
import com.example.foodordering.repository.FoodRepository;
import com.example.foodordering.repository.OrderMenuItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service to manage cart operations including adding, updating, retrieving, and deleting cart items.
 */
@Service
@Transactional
public class CartService {

    private final OrderMenuItemRepository orderMenuItemRepository;
    private final FoodRepository foodRepository;
    private final CustomerRepository customerRepository;
    private final FoodOrderRepository foodOrderRepository; // Inject FoodOrderRepository
    private final OrderStatusRepository orderStatusRepository;

    public CartService(OrderMenuItemRepository orderMenuItemRepository, FoodRepository foodRepository,
                       CustomerRepository customerRepository, FoodOrderRepository foodOrderRepository,
                       OrderStatusRepository orderStatusRepository) {
        this.orderMenuItemRepository = orderMenuItemRepository;
        this.foodRepository = foodRepository;
        this.customerRepository = customerRepository;
        this.foodOrderRepository = foodOrderRepository;
        this.orderStatusRepository = orderStatusRepository;
    }

    /**
     * Retrieves the cart items for a specific customer.
     *
     * @return ApiResponse containing cart items or an empty list if the cart is empty.
     */
    public ApiResponse<List<OrderMenuItem>> getCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            long accountId = userDetails.getAccountId(); // Đúng kiểu dữ liệu
            List<FoodOrder> cartOrders = foodOrderRepository.findByCustomer_idAndOrderStatus_StatusValue(
                    (int) accountId,
                    false
            );

            if (!cartOrders.isEmpty()) {
                List<OrderMenuItem> cartItems = orderMenuItemRepository.findByFoodOrder_Id(cartOrders.getFirst().getId());
                return ApiResponse.build(1000, "Success", cartItems);
            }
        }
        return ApiResponse.build(1000, "Success", List.of());
    }


    /**
     * Adds an item to the cart.
     *
     * @param request the request containing customerId, foodId, and quantity.
     * @return ApiResponse containing the added cart item.
     */
    public ApiResponse<OrderMenuItem> addItemToCart(CartItemRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));

        Food food = foodRepository.findById(request.getFoodId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Food not found"));

        // Tìm giỏ hàng hiện tại của khách hàng
        List<FoodOrder> existingCartOrders = foodOrderRepository.findByCustomer_idAndOrderStatus_StatusValue(customer.getId(), false);
        FoodOrder cartOrder;

        if (!existingCartOrders.isEmpty()) {
            cartOrder = existingCartOrders.getFirst();
        } else {
            // Tạo một FoodOrder mới nếu chưa có giỏ hàng
            cartOrder = new FoodOrder();
            cartOrder.setCustomer(customer);
            // Giả sử bạn có OrderStatus với Status value là false (ví dụ, ID là 1)
            OrderStatus cartOrderStatus = orderStatusRepository.findById(1)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cart Order Status not found"));
            cartOrder.setOrderStatus(cartOrderStatus);
            cartOrder.setTotalItems(0);
            cartOrder.setPrice(BigDecimal.ZERO);
            cartOrder = foodOrderRepository.save(cartOrder);
        }

        // Kiểm tra xem món ăn đã có trong giỏ hàng chưa
        Optional<OrderMenuItem> existingCartItem = cartOrder.getOrderMenuItems().stream()
                .filter(item -> item.getFood().getId() == food.getId())
                .findFirst();

        OrderMenuItem cartItem;
        if (existingCartItem.isPresent()) {
            // Nếu món ăn đã có, tăng số lượng
            cartItem = existingCartItem.get();
            cartItem.setQuantityOrdered(cartItem.getQuantityOrdered() + request.getQuantity());
        } else {
            // Nếu chưa có, tạo một OrderMenuItem mới
            cartItem = new OrderMenuItem();
            cartItem.setFood(food);
            cartItem.setQuantityOrdered(request.getQuantity());
            cartItem.setFoodOrder(cartOrder);
            orderMenuItemRepository.save(cartItem);
            if (cartOrder.getOrderMenuItems() == null) {
                cartOrder.setOrderMenuItems(List.of(cartItem));
            } else {
                cartOrder.getOrderMenuItems().add(cartItem);
            }
        }

        // Cập nhật totalItems và price của FoodOrder
        updateCartTotals(cartOrder);

        return ApiResponse.build(1201, "Success", cartItem);
    }


    /**
     * Updates the quantity of an item in the cart.
     *
     * @param cartItemId  ID of the cart item.
     * @param newQuantity the new quantity.
     * @return ApiResponse containing the updated cart item.
     */
    public ApiResponse<OrderMenuItem> updateItemQuantity(int cartItemId, int newQuantity) {
        OrderMenuItem cartItem = orderMenuItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart item not found"));

        FoodOrder cartOrder = cartItem.getFoodOrder();
        List<FoodOrder> customerCartOrders = foodOrderRepository.findByCustomer_idAndOrderStatus_StatusValue(cartOrder.getCustomer().getId(), false);

        if (customerCartOrders.isEmpty() || !(customerCartOrders.getFirst().getId() == cartOrder.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cart item does not belong to the current customer's cart");
        }

        cartItem.setQuantityOrdered(newQuantity);
        orderMenuItemRepository.save(cartItem);

        // Cập nhật totalItems và price trong FoodOrder tương ứng
        updateCartTotals(cartOrder);

        return ApiResponse.build(1000, "Success", cartItem);
    }

    /**
     * Removes an item from the cart.
     *
     * @param cartItemId ID of the cart item to be removed.
     * @return ApiResponse indicating success or failure.
     */
    public ApiResponse<String> removeItemFromCart(int cartItemId) {
        OrderMenuItem cartItem = orderMenuItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart item not found"));

        FoodOrder cartOrder = cartItem.getFoodOrder();
        List<FoodOrder> customerCartOrders = foodOrderRepository.findByCustomer_idAndOrderStatus_StatusValue(cartOrder.getCustomer().getId(), false);

        if (customerCartOrders.isEmpty() || !(customerCartOrders.getFirst().getId() == cartOrder.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cart item does not belong to the current customer's cart");
        }

        orderMenuItemRepository.deleteById(cartItemId);

        // Cập nhật totalItems và price trong FoodOrder
        updateCartTotals(cartOrder);

        return ApiResponse.build(1000, "Success", "Deleted");
    }

    /**
     * Clears the entire cart for a specific customer.
     *
     * @return ApiResponse indicating success.
     */
    public ApiResponse<String> clearCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();
        int accountId = (int) account.getAccountId();

        List<FoodOrder> cartOrders = foodOrderRepository.findByCustomer_idAndOrderStatus_StatusValue(accountId, false);
        if (!cartOrders.isEmpty()) {
            orderMenuItemRepository.deleteByFoodOrder_Id(cartOrders.getFirst().getId());
            FoodOrder order = cartOrders.getFirst();
            order.setTotalItems(0);
            order.setPrice(BigDecimal.ZERO);
            foodOrderRepository.save(order);
            return ApiResponse.build(1000, "Success", "Cart Cleared");
        } else {
            return ApiResponse.build(1404, "Failed", "Cart not found for this customer");
        }
    }

    private void updateCartTotals(FoodOrder cartOrder) {
        int totalItems = cartOrder.getOrderMenuItems() == null ? 0 : cartOrder.getOrderMenuItems().stream().mapToInt(OrderMenuItem::getQuantityOrdered).sum();
        BigDecimal totalPrice = cartOrder.getOrderMenuItems() == null ? BigDecimal.ZERO : cartOrder.getOrderMenuItems().stream()
                .map(item -> item.getFood().getPrice().multiply(BigDecimal.valueOf(item.getQuantityOrdered())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cartOrder.setTotalItems(totalItems);
        cartOrder.setPrice(totalPrice);
        foodOrderRepository.save(cartOrder);
    }
}