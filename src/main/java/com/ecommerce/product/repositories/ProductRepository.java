package com.ecommerce.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.model.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>,
        org.springframework.data.jpa.repository.JpaSpecificationExecutor<Product> {
    boolean existsByName(String name);

    Page<Product> findAll(Pageable pageable);

    List<Product> findByIsFeaturedTrue();
}
