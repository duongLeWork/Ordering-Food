package com.example.foodordering.entity;

import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails.Address;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


@Entity
@Data
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer restaurantId;
    private String restaurantName; // Lỗi chính tả trong sơ đồ, tôi giữ nguyên hoặc bạn có thể sửa thành restaurantName

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

    // Lưu ý: FK Menu Item_ID có vẻ không hợp lý, có lẽ bạn muốn liên kết với Food?
    // Nếu đúng, bạn có thể thêm quan hệ như sau:
    // @OneToMany
    // @JoinColumn(name = "restaurant_id")
    // private List<Food> foods;
    // Hoặc nếu là một bảng Menu riêng biệt:
    // @ManyToOne
    // @JoinColumn(name = "menu_item_id")
    // private MenuItem menuItem; // Giả sử bạn có class MenuItem
}