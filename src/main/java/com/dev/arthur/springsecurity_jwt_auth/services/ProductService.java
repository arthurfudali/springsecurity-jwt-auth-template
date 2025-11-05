package com.dev.arthur.springsecurity_jwt_auth.services;

import com.dev.arthur.springsecurity_jwt_auth.entities.Product;
import com.dev.arthur.springsecurity_jwt_auth.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        Optional<Product> obj = productRepository.findById(id);
        return obj.orElseThrow(() -> new RuntimeException(" Product not found with id " + id));
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }


}
