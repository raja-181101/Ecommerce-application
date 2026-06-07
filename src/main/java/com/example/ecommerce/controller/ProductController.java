package com.example.ecommerce.controller;

import com.example.ecommerce.dataToobject.ProductDTO;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService service;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ProductDTO addProduct(@Valid @RequestBody ProductDTO product){
        return service.saveProduct(product);
    }
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping
    public Page<ProductDTO> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "productName") String sortBy){
        return service.getAllProducts(page, size,sortBy);
    }
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{id}")
    public ProductDTO getProductById(@PathVariable Long id){
        return service.getProductFromId(id);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ProductDTO updateProduct(@PathVariable Long id, @RequestBody ProductDTO newProduct){
        return service.updateProduct(id,newProduct);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public String deleteProductById(@PathVariable Long id){
        service.deleteProductById(id);
        return "Deleted Successfully";
    }
}
