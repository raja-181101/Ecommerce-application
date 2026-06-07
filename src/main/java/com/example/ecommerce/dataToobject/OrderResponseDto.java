package com.example.ecommerce.dataToobject;

import lombok.Data;

@Data
public class OrderResponseDto {
    private Long orderId;
    private String orderStatus;
    private Long userId;
    private String userName;
}
