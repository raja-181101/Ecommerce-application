package com.example.ecommerce.dataToobject;

import lombok.Data;

@Data
public class AuthResponse {
    public String accessToken;
    public String refreshToken;
}
