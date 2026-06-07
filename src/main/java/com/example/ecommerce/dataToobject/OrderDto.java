package com.example.ecommerce.dataToobject;

import lombok.Data;

@Data
public class OrderDto {
    private Long orderId;
    private Long productId;
    private int quantity;
    private Long userId;
    private String userName;
}
