package com.example.mosabbir.controller;

import com.example.mosabbir.entity.Product;
import com.example.mosabbir.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // USER and ADMIN can read products
    @GetMapping
    @PreAuthorize("hasAuthority('PRODUCT_READ')")
    public List<Product> getAllProducts() {
        System.out.println(" Getting all products - USER/ADMIN access");
        return productService.getAllProducts();
    }

    // USER and ADMIN can read specific product
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PRODUCT_READ')")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        System.out.println(" Getting product by ID - USER/ADMIN access");
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Only ADMIN can create products
    @PostMapping
    @PreAuthorize("hasAuthority('PRODUCT_WRITE')")
    public Product createProduct(@RequestBody Product product) {
        System.out.println(" Creating product - ADMIN only access");
        return productService.createProduct(product);
    }

    // Only ADMIN can update products
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PRODUCT_UPDATE')")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        System.out.println(" Updating product - ADMIN only access");
        try {
            Product updatedProduct = productService.updateProduct(id, productDetails);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Only ADMIN can delete products
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PRODUCT_DELETE')")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        System.out.println(" Deleting product - ADMIN only access");
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}