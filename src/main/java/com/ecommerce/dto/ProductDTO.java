package com.ecommerce.dto;

import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private String imageUrl;
    private String category;
    private String brand;
    private Boolean isFeatured;
    private Boolean isActive;

    // Read-only / Output fields
    private Double rating;
    private Integer numReviews;
    private Date createdAt;
}
