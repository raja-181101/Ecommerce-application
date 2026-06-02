package com.example.ecommerce.service;


import com.example.ecommerce.dataToobject.ProductDTO;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository repo;
    public ProductService(ProductRepository repo){
        this.repo = repo;
    }

    public ProductDTO saveProduct(ProductDTO productDTO){
        Product product = new Product();
        product.setProductName(productDTO.getProductName());
        product.setPrice(productDTO.getProductPrice());
        Product saveProduct = repo.save(product);
        return convertToDTO(saveProduct);
    }

    public List<ProductDTO> getAllProducts(){
        List<Product> products = repo.findAll();
        return products.stream().map(this::convertToDTO).toList();
    }
    public ProductDTO getProductFromId(Long id){
        Product product = repo.findById(id).orElseThrow(()->new ResourceNotFoundException("Product Not Found"));
        return convertToDTO(product);
    }

    public ProductDTO updateProduct(Long id, ProductDTO newProduct){
        Product existingProduct = repo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Product Not Found"));
        existingProduct.setProductName(newProduct.getProductName());
        existingProduct.setPrice(newProduct.getProductPrice());
        Product updateProduct = repo.save(existingProduct);
        return convertToDTO(updateProduct);
    }

    public void deleteProductById(Long id){
        if(!repo.existsById(id)){
            throw new ResourceNotFoundException("Product Not Found");
        }
        repo.deleteById(id);
    }
    private ProductDTO convertToDTO(Product cProduct){
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductName(cProduct.getProductName());
        productDTO.setProductPrice(cProduct.getPrice());
        return productDTO;
    }
}
