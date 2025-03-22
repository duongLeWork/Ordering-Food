# Danh sách các Functions trong từng Service một
Cái này thì nhờ ChatGPT tổng hợp lại ;-; cho dev đọc, sẽ tốt hơn nếu thêm phần này trong javadoc, nhưng để thêm sau vậy.

### 1. GuestService
- **Chức năng:** Cung cấp các tính năng cho khách truy cập (chưa đăng nhập).  
- **Phương thức:**  
    - `viewHomepage()`: Hiển thị trang chủ.  
    - `searchDishes(String keyword)`: Tìm kiếm món ăn theo từ khóa.  
    - `createAccount(UserDTO userDTO)`: Đăng ký tài khoản mới.  

---

### 2. AuthService
- **Chức năng:** Xử lý xác thực và đăng nhập.  
- **Phương thức:**  
    - `login(String username, String password)`: Xác thực người dùng.  
    - `resetPassword(String email)`: Gửi email đặt lại mật khẩu.  
    - `verifyAccount(String token)`: Xác minh tài khoản thông qua email.  

---

### 3. CustomerService
- **Chức năng:** Cung cấp dịch vụ cho khách hàng đã đăng nhập.  
- **Phương thức:**  
    - `editProfile(UserDTO userDTO)`: Chỉnh sửa thông tin cá nhân.  
    - `addDishesToCart(int dishId, int quantity)`: Thêm món vào giỏ hàng.  
    - `updateCart(int dishId, int newQuantity)`: Cập nhật số lượng món trong giỏ hàng.  
    - `removeDishFromCart(int dishId)`: Xóa món khỏi giỏ hàng.  
    - `confirmOrder(int cartId)`: Xác nhận đặt hàng.  
    - `payForOrder(int orderId, PaymentDTO paymentInfo)`: Thanh toán đơn hàng.  
    - `trackOrder(int orderId)`: Theo dõi trạng thái đơn hàng.  
    - `showOrderDetails(int orderId)`: Xem chi tiết đơn hàng.  

---

### 4. OrderService
- **Chức năng:** Quản lý đơn hàng.  
- **Phương thức:**  
    - `createOrder(int customerId, List<OrderItemDTO> items)`: Tạo đơn hàng mới.  
    - `updateOrderStatus(int orderId, OrderStatus status)`: Cập nhật trạng thái đơn hàng.  
    - `getOrderDetails(int orderId)`: Lấy thông tin chi tiết đơn hàng.  

---

### 5. ManagerService
- **Chức năng:** Cung cấp dịch vụ cho người quản lý nhà hàng.  
- **Phương thức:**  
    - `manageUsers(UserDTO userDTO)`: Quản lý tài khoản người dùng.  
    - `receiveOrders()`: Nhận danh sách đơn hàng cần xử lý.  
    - `manageMenu(DishDTO dishDTO)`: Quản lý thực đơn (thêm, sửa, xóa món ăn).  
    - `sendEmailForAdvertisement(String emailContent)`: Gửi email quảng cáo.  
    - `viewSalesReport(Date startDate, Date endDate)`: Xem báo cáo doanh số.  

---

### 6. CartService
- **Chức năng:** Quản lý giỏ hàng của khách hàng.  
- **Phương thức:**  
    - `getCart(int customerId)`: Lấy giỏ hàng của khách hàng.  
    - `addItemToCart(int customerId, int dishId, int quantity)`: Thêm món vào giỏ hàng.  
    - `removeItemFromCart(int customerId, int dishId)`: Xóa món khỏi giỏ hàng.  
    - `clearCart(int customerId)`: Xóa toàn bộ giỏ hàng.  

---

### 7. PaymentService
Hiện tại chưa được thêm do nó khá phức tạp

- **Chức năng:** Xử lý thanh toán.  
- **Phương thức:**  
    - `processPayment(int orderId, PaymentDTO paymentDetails)`: Xử lý thanh toán cho đơn hàng.  
    - Chưa có gì khác
---

### Tóm tắt danh sách Service

| **STT** | **Service**          | **Chức năng chính**  |
|---------|----------------------|----------------------|
| 1       | `GuestService`        | Xem trang chủ, tìm món, tạo tài khoản |
| 2       | `AuthService`         | Đăng nhập, xác minh tài khoản, đặt lại mật khẩu |
| 3       | `CustomerService`     | Quản lý hồ sơ, giỏ hàng, đặt hàng, thanh toán |
| 4       | `OrderService`        | Quản lý đơn hàng |
| 5       | `ManagerService`      | Quản lý người dùng, đơn hàng, thực đơn, báo cáo |
| 6       | `CartService`         | Quản lý giỏ hàng |
| 7       | `PaymentService`      | Xử lý thanh toán |
