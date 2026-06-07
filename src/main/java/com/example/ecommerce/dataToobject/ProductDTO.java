package com.example.ecommerce.dataToobject;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProductDTO {
    @NotBlank
    public String productName;
    @Positive
    public Double productPrice;
    @Positive
    public int quantity;

}
