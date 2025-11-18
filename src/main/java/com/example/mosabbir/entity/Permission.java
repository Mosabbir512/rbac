package com.example.mosabbir.entity;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.*;
import lombok.Data;
//import javax.persistence.*;

@Entity
@Table(name = "permissions")
@Data
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name; // e.g., PRODUCT_READ, PRODUCT_WRITE, PRODUCT_UPDATE, PRODUCT_DELETE

    private String description;
}