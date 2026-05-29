package com.example.ecommerce.service;


import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repo;

    public Product saveProduct(Product product){
        return repo.save(product);
    }

    public List<Product> getAllProducts(){
        return repo.findAll();
    }
    public Product getProductFromId(Long id){
        return repo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Product Not Found"));
    }

    public Product updateProduct(Long id, Product newProduct){
        Product existingProduct = repo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Product Not Found"));
        existingProduct.setProductName(newProduct.getProductName());
        existingProduct.setPrice(newProduct.getPrice());
        return repo.save(existingProduct);
    }

    public void deleteProductById(Long id){
        if(!repo.existsById(id)){
            throw new ResourceNotFoundException("Product Not Found");
        }
        repo.deleteById(id);
    }
}
