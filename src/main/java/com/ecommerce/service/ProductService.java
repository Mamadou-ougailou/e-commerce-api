package com.ecommerce.service;

import com.ecommerce.model.Product;
import com.ecommerce.dto.ProductDTO;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.mapper.ProductMapper;
import com.ecommerce.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    public Page<ProductDTO> getAllProducts(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return productRepository.findAll(pageable).map(productMapper::toDTO);
    }

    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return productMapper.toDTO(product);
    }

    public ProductDTO createProduct(ProductDTO productDTO) {
        if (productRepository.existsByName(productDTO.getName())) {
            throw new RuntimeException("Product with this name already exists");
        }
        Product product = productMapper.toEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        return productMapper.toDTO(savedProduct);
    }

    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        // Only check name uniqueness if name is being changed
        if (productDTO.getName() != null && !productDTO.getName().equals(existingProduct.getName())) {
            if (productRepository.existsByName(productDTO.getName())) {
                throw new RuntimeException("Product with this name already exists");
            }
        }

        productMapper.updateEntityFromDTO(existingProduct, productDTO);
        Product savedProduct = productRepository.save(existingProduct);
        return productMapper.toDTO(savedProduct);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    public Long countProducts() {
        return productRepository.count();
    }

    public Page<ProductDTO> searchProducts(String category, Double minPrice, Double maxPrice, String keyword,
            Pageable pageable) {
        return productRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (category != null)
                predicates.add(cb.equal(root.get("category"), category));
            if (minPrice != null)
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
            if (maxPrice != null)
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
            if (keyword != null && !keyword.isEmpty()) {
                String searchPattern = "%" + keyword.toLowerCase() + "%";
                Predicate namePredicate = cb.like(cb.lower(root.get("name")), searchPattern);
                Predicate descriptionPredicate = cb.like(cb.lower(root.get("description")), searchPattern);
                Predicate categoryPredicate = cb.like(cb.lower(root.get("category")), searchPattern);
                Predicate brandPredicate = cb.like(cb.lower(root.get("brand")), searchPattern);

                // Combine them with OR
                predicates.add(cb.or(namePredicate, descriptionPredicate, categoryPredicate, brandPredicate));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable).map(productMapper::toDTO);
    }

    public ProductDTO uploadProductImage(Long id, MultipartFile file) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        existingProduct.setImageUrl(file.getOriginalFilename());
        Product savedProduct = productRepository.save(existingProduct);
        return productMapper.toDTO(savedProduct);
    }

    public List<ProductDTO> getFeaturedProducts() {
        return productRepository.findByIsFeaturedTrue().stream().map(productMapper::toDTO).toList();
    }
}
