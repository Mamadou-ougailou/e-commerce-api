package com.ecommerce.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date createdAt = new Date(); // Initialize properly
    private String name;
    private String description;
    private double price;
    private int stock;
    private String imageUrl;
    private String category;
    private String brand;
    private double rating;
    private int numReviews;
    private boolean isFeatured;
    private boolean isActive;

    // Custom constructor if needed for specific logic (optional with lombok but
    // good for partial inits)
    public Product(String name, String description, double price, int stock, String imageUrl) {
        this.createdAt = new Date();
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
    }
}
