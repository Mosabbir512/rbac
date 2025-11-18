package com.example.mosabbir.repository;



import com.example.mosabbir.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}