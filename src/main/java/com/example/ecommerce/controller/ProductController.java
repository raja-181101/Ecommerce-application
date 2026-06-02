package com.example.ecommerce.controller;

import com.example.ecommerce.dataToobject.ProductDTO;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService service;

    @PostMapping
    public ProductDTO addProduct(@Valid @RequestBody ProductDTO product){
        return service.saveProduct(product);
    }

    @GetMapping
    public List<ProductDTO> getProducts(){
        return service.getAllProducts();
    }

    @GetMapping("/{id}")
    public ProductDTO getProductById(@PathVariable Long id){
        return service.getProductFromId(id);
    }

    @PutMapping("/{id}")
    public ProductDTO updateProduct(@PathVariable Long id, @RequestBody ProductDTO newProduct){
        return service.updateProduct(id,newProduct);
    }

    @DeleteMapping("/{id}")
    public String deleteProductById(@PathVariable Long id){
        service.deleteProductById(id);
        return "Deleted Successfully";
    }
}
