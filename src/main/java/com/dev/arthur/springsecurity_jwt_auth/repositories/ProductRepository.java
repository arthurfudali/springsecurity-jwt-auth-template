package com.dev.arthur.springsecurity_jwt_auth.repositories;

import com.dev.arthur.springsecurity_jwt_auth.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
