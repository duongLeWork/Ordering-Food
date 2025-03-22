#!/bin/bash

# API base URL
BASE_URL="http://localhost:8080"

# Sample data
USERNAME="testuser"
PASSWORD="password123"
EMAIL="testuser@example.com"
CUSTOMER_ID=1
FOOD_ID=1
ORDER_ID=1
MANAGER_ROLE="MANAGER"

# Function to send requests
send_request() {
    local test_name="$1"
    local method="$2"
    local endpoint="$3"
    local data="$4"
    local expected_result="$5"

    echo "🔹 Testing: $test_name"
    echo "📌 Expected: $expected_result"

    actual_result=$(curl -s -X $method "$BASE_URL$endpoint" -H "Content-Type: application/json" -d "$data")
    echo "✅ Actual: $actual_result"

    echo -e "-------------------------------------------------\n"
}

# 🗑️ Clean up database before testing
echo "🗑️ Cleaning up database before tests..."
cleanup_response=$(curl -s -X DELETE "$BASE_URL/test/cleanup")

if [[ "$cleanup_response" != *"Database cleaned!"* ]]; then
    echo "❌ Cleanup failed! Exiting..."
    exit 1
fi

echo "✅ Database cleaned!"

# 🛠️ Insert test data
echo "🛠️ Inserting test data..."
REGISTER_RESPONSE=$(curl -s -X POST "$BASE_URL/guest/register" -H "Content-Type: application/json" -d '{
    "username": "'"$USERNAME"'",
    "password": "'"$PASSWORD"'",
    "email": "'"$EMAIL"'",
    "role": "CUSTOMER"
}')
CUSTOMER_ID=$(echo $REGISTER_RESPONSE | jq -r '.data.accountId')

FOOD_RESPONSE=$(curl -s -X POST "$BASE_URL/manager/food" -H "Content-Type: application/json" -d '{
    "name": "Pizza",
    "price": 10.99,
    "description": "Delicious cheese pizza",
    "image": "pizza.jpg",
    "isAvailable": true
}')
FOOD_ID=$(echo $FOOD_RESPONSE | jq -r '.data.foodId')

echo "✅ Test data inserted! CUSTOMER_ID=$CUSTOMER_ID, FOOD_ID=$FOOD_ID"

echo "🚀 STARTING API TESTS..."

# 1️⃣ Guest API
send_request "GET /guest/dishes" "GET" "/guest/dishes" "" '{"code": 1000, "message": "Danh sách món ăn có sẵn"}'
send_request "GET /guest/search" "GET" "/guest/search?keyword=pizza" "" '{"code": 1000, "message": "Danh sách món ăn tìm thấy"}'

# 2️⃣ Auth API
send_request "POST /auth/login" "POST" "/auth/login" \
'{"username": "'"$USERNAME"'", "password": "'"$PASSWORD"'"}' '{"code": 1000, "message": "Đăng nhập thành công!"}'

# 6️⃣ Manager API
send_request "DELETE /manager/food" "DELETE" "/manager/food/$FOOD_ID" "" '{"code": 1000, "message": "Món ăn đã bị xóa!"}'

echo "✅ API TESTS COMPLETED!"

# 🗑️ Clean up database after testing
echo "🗑️ Cleaning up database after tests..."
curl -s -X DELETE "$BASE_URL/test/cleanup"
echo "✅ Database cleaned!"
