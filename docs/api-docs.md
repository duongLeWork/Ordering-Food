# Hướng dẫn cơ bản cách sử dụng API

## Hướng dẫn chung

### Thông tin cơ bản cho API

Mô tả: API hỗ trợ khách hàng đặt hàng, quản lý giỏ hàng, tài khoản, quản lý nhà hàng.  

Cấu trúc Response chuẩn: (Khi sử dụng Postman hoặc tương tự). Chuẩn hóa tất cả API theo `ApiResponse<T>` (Giống anh Dương ;-;, có sai chắc do tui nhầm)


```json
{
    "code": 1000,
    "message": "Success",
    "data": {...} 
}
```
- `code`: Mã trạng thái của API (`1000` = Thành công, `1404` = Không tìm thấy, `1201` = Tạo thành công, `1400` = Lỗi dữ liệu)  
- `message`: Mô tả kết quả xử lý  
- `data`: Dữ liệu trả về  

Dưới đây là danh sách tất cả các **API Endpoint** của hệ thống **Food Ordering API**, được phân loại theo từng module.  

> **Mẹo không hay**: Có thể chạy `test-api.sh` nếu bạn dùng MacOS hoặc Linux. Lưu ý là mỗi lần chạy nó sẽ xóa sạch cơ sở dữ liệu và khởi tạo các dữ liệu mẫu khác =)) Sẽ sửa cái này trong tương lai gần. 

### 1. Guest API (Người dùng chưa đăng nhập)

| Method | Endpoint | Mô tả |
|--||-|
| **GET** | `/guest/dishes` | Lấy danh sách món ăn có sẵn |
| **GET** | `/guest/search?keyword={keyword}` | Tìm kiếm món ăn theo từ khóa |
| **POST** | `/guest/register` | Đăng ký tài khoản |



### 2. Auth API (Xác thực & Đăng nhập)

| Method | Endpoint | Mô tả |
|--||-|
| **POST** | `/auth/login` | Đăng nhập vào hệ thống |
| **POST** | `/auth/reset-password` | Đặt lại mật khẩu qua email |



### 3. Customer API (Dành cho khách hàng đã đăng nhập)

| Method | Endpoint | Mô tả |
|--||-|
| **PUT** | `/customer/update-profile/{customerId}` | Cập nhật thông tin cá nhân |
| **POST** | `/customer/place-order` | Đặt hàng mới |
| **GET** | `/customer/track-order/{orderId}` | Theo dõi trạng thái đơn hàng |
| **GET** | `/customer/order-details/{orderId}` | Xem chi tiết đơn hàng |



### 4. Cart API (Giỏ hàng)

| Method | Endpoint | Mô tả |
|--||-|
| **GET** | `/cart/{customerId}` | Xem giỏ hàng của khách hàng |
| **POST** | `/cart/add` | Thêm món vào giỏ hàng |
| **PUT** | `/cart/update/{cartItemId}?newQuantity={newQuantity}` | Cập nhật số lượng món trong giỏ hàng |
| **DELETE** | `/cart/remove/{cartItemId}` | Xóa một món khỏi giỏ hàng |
| **DELETE** | `/cart/clear/{customerId}` | Xóa toàn bộ giỏ hàng của khách hàng |



### 5. Order API (Quản lý đơn hàng)

| Method | Endpoint | Mô tả |
|--||-|
| **POST** | `/orders/create` | Tạo đơn hàng mới |
| **PUT** | `/orders/update-status` | Cập nhật trạng thái đơn hàng |
| **GET** | `/orders/customer/{customerId}` | Lấy danh sách đơn hàng theo khách hàng |
| **GET** | `/orders/details/{orderId}` | Xem chi tiết đơn hàng |



### 6. Manager API (Quản lý nhà hàng)

| Method | Endpoint | Mô tả |
|--||-|
| **GET** | `/manager/users/{role}` | Lấy danh sách người dùng theo vai trò (MANAGER, CUSTOMER) |
| **GET** | `/manager/orders` | Lấy danh sách tất cả đơn hàng |
| **POST** | `/manager/food` | Thêm món ăn vào thực đơn |
| **PUT** | `/manager/food/{foodId}` | Cập nhật món ăn trong thực đơn |
| **DELETE** | `/manager/food/{foodId}` | Xóa món ăn khỏi thực đơn |
| **GET** | `/manager/sales` | Xem tổng doanh thu từ đơn hàng |


## 1. Guest API (Người dùng chưa đăng nhập)
### Lấy danh sách món ăn có sẵn
- Endpoint: `GET /guest/dishes`  
- Mô tả: Trả về danh sách món ăn có sẵn  
- Response:  

```json
{
    "code": 1000,
    "message": "Danh sách món ăn có sẵn",
    "data": [
        { "foodId": 1, "name": "Pizza", "price": 12.99 },
        { "foodId": 2, "name": "Burger", "price": 9.99 }
    ]
}
```

### Tìm kiếm món ăn theo từ khóa
- Endpoint: `GET /guest/search?keyword=pizza`  
- Response (Nếu tìm thấy món ăn):  

```json
{
    "code": 1000,
    "message": "Danh sách món ăn tìm thấy",
    "data": [{ "foodId": 1, "name": "Pizza", "price": 12.99 }]
}
```
- Response (Không tìm thấy món ăn):  

```json
{
    "code": 1404,
    "message": "Không tìm thấy món ăn nào!",
    "data": null
}
```

### Đăng ký tài khoản
- Endpoint: `POST /guest/register`  
- Body Request:  

```json
{
    "username": "newuser",
    "password": "password123",
    "email": "newuser@example.com",
    "role": "CUSTOMER"
}
```
- Response (Thành công):  

```json
{
    "code": 1201,
    "message": "Tài khoản đã được tạo thành công!",
    "data": { "accountId": 5, "username": "newuser" }
}
```



## 2️. Auth API (Xác thực)
### Đăng nhập
- Endpoint: `POST /auth/login`  
- Body Request:  

```json
{
    "username": "testuser",
    "password": "password123"
}
```
- Response (Thành công):  

```json
{
    "code": 1000,
    "message": "Đăng nhập thành công!",
    "data": { "accountId": 1, "username": "testuser" }
}
```

### Đặt lại mật khẩu
- Endpoint: `POST /auth/reset-password`  
- Body Request:  

```json
{
    "email": "test@example.com",
    "newPassword": "newpassword123"
}
```
- Response (Thành công):  

```json
{
    "code": 1200,
    "message": "Mật khẩu đã được đặt lại thành công!",
    "data": "Success"
}
```



## 3️. Customer API (Khách hàng đã đăng nhập)
### Đặt hàng
- Endpoint: `POST /customer/place-order`  
- Body Request:  

```json
{
    "customerId": 1,
    "totalAmount": 22.98
}
```
- Response (Thành công):  

```json
{
    "code": 1201,
    "message": "Đơn hàng đã được tạo thành công!",
    "data": { "foodOrderId": 5, "totalAmount": 22.98, "orderStatus": "Pending" }
}
```

### Theo dõi trạng thái đơn hàng
- Endpoint: `GET /customer/track-order/5`  
- Response:  

```json
{
    "code": 1000,
    "message": "Trạng thái đơn hàng",
    "data": "Pending"
}
```

## Order API (Quản lý đơn hàng)  
### Tạo đơn hàng
- Endpoint: `POST /orders/create`  
- Body Request:  

```json
{
    "customerId": 1,
    "totalAmount": 25.99
}
```
- Response (Thành công):  

```json
{
    "code": 1201,
    "message": "Đơn hàng đã được tạo thành công!",
    "data": {
        "foodOrderId": 10,
        "customer": { "customerId": 1 },
        "totalAmount": 25.99,
        "orderStatus": { "statusValue": "Pending" }
    }
}
```
- Response (Khách hàng không tồn tại):  

```json
{
    "code": 1404,
    "message": "Khách hàng không tồn tại!",
    "data": null
}
```



### Cập nhật trạng thái đơn hàng
- Endpoint: `PUT /orders/update-status`  
- Body Request:  

```json
{
    "orderId": 10,
    "newStatus": "Completed"
}
```
- Response (Thành công):  

```json
{
    "code": 1000,
    "message": "Cập nhật trạng thái đơn hàng thành công!",
    "data": {
        "foodOrderId": 10,
        "orderStatus": { "statusValue": "Completed" }
    }
}
```
- Response (Không tìm thấy đơn hàng):  

```json
{
    "code": 1404,
    "message": "Không tìm thấy đơn hàng!",
    "data": null
}
```



### Lấy danh sách đơn hàng theo khách hàng
- Endpoint: `GET /orders/customer/{customerId}`  
- Ví dụ: `GET /orders/customer/1`  
- Response:  

```json
{
    "code": 1000,
    "message": "Danh sách đơn hàng của khách hàng",
    "data": [
        {
            "foodOrderId": 5,
            "totalAmount": 19.99,
            "orderStatus": { "statusValue": "Completed" }
        },
        {
            "foodOrderId": 10,
            "totalAmount": 25.99,
            "orderStatus": { "statusValue": "Pending" }
        }
    ]
}
```



### Xem chi tiết đơn hàng
- Endpoint: `GET /orders/details/{orderId}`  
- Ví dụ: `GET /orders/details/10`  
- Response (Nếu đơn hàng tồn tại):  

```json
{
    "code": 1000,
    "message": "Chi tiết đơn hàng",
    "data": {
        "foodOrderId": 10,
        "customer": { "customerId": 1 },
        "totalAmount": 25.99,
        "orderStatus": { "statusValue": "Pending" }
    }
}
```
- Response (Không tìm thấy đơn hàng):  

```json
{
    "code": 1404,
    "message": "Không tìm thấy đơn hàng!",
    "data": null
}
```


## Cart API (Giỏ hàng)
### Xem giỏ hàng
- Endpoint: `GET /cart/1`  
- Response:  

```json
{
    "code": 1000,
    "message": "Giỏ hàng của khách hàng",
    "data": [{ "orderMenuId": 1, "food": { "foodId": 5, "name": "Pizza" }, "quantityOrdered": 2 }]
}
```

### Thêm món vào giỏ hàng
- Endpoint: `POST /cart/add`  
- Body Request:  

```json
{
    "customerId": 1,
    "foodId": 5,
    "quantity": 2
}
```
- Response:  

```json
{
    "code": 1201,
    "message": "Đã thêm món vào giỏ hàng!",
    "data": { "orderMenuId": 2, "food": { "foodId": 5, "name": "Pizza" }, "quantityOrdered": 2 }
}
```

### Cập nhật số lượng món ăn
- Endpoint: `PUT /cart/update/2?newQuantity=3`  
- Response:  

```json
{
    "code": 1000,
    "message": "Cập nhật số lượng thành công!",
    "data": { "orderMenuId": 2, "quantityOrdered": 3 }
}
```



## Manager API (Quản lý nhà hàng)
### Lấy danh sách đơn hàng
- Endpoint: `GET /manager/orders`  
- Response:  

```json
{
    "code": 1000,
    "message": "Danh sách tất cả đơn hàng",
    "data": [{ "foodOrderId": 5, "totalAmount": 19.99, "orderStatus": "Completed" }]
}
```

### Thêm món ăn mới
- Endpoint: `POST /manager/food`  
- Body Request:  

```json
{
    "name": "Spaghetti",
    "price": 12.99,
    "description": "Italian pasta",
    "image": "spaghetti.jpg",
    "isAvailable": true
}
```
- Response:  

```json
{
    "code": 1201,
    "message": "Món ăn đã được thêm thành công!",
    "data": { "foodId": 7, "name": "Spaghetti", "price": 12.99 }
}
```



