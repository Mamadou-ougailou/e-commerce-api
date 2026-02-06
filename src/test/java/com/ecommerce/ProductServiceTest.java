package com.ecommerce;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.ecommerce.repositories.ProductRepository;
import com.ecommerce.service.ProductService;
import com.ecommerce.model.Product;
import com.ecommerce.dto.ProductDTO;
import com.ecommerce.mapper.ProductMapper;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    @Test
    void shouldReturnProduct_WhenIdExists() {
        // Arrange
        Product fakeProduct = new Product();
        fakeProduct.setId(1L);
        fakeProduct.setName("Test Laptop");

        ProductDTO fakeDTO = new ProductDTO();
        fakeDTO.setId(1L);
        fakeDTO.setName("Test Laptop");

        when(productRepository.findById(1L)).thenReturn(Optional.of(fakeProduct));
        when(productMapper.toDTO(fakeProduct)).thenReturn(fakeDTO);

        // Act
        ProductDTO result = productService.getProductById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Test Laptop", result.getName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowException_WhenProductNotFound() {
        // Arrange
        when(productRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            productService.deleteProduct(99L);
        });
    }

    @Test
    void shouldCreateProduct_WhenNameIsUnique() {
        // Arrange
        ProductDTO inputDTO = new ProductDTO();
        inputDTO.setName("New Product");

        Product productEntity = new Product();
        productEntity.setName("New Product");

        Product savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setName("New Product");

        when(productRepository.existsByName("New Product")).thenReturn(false);
        when(productMapper.toEntity(inputDTO)).thenReturn(productEntity);
        when(productRepository.save(productEntity)).thenReturn(savedProduct);
        when(productMapper.toDTO(savedProduct)).thenReturn(inputDTO);

        // Act
        ProductDTO result = productService.createProduct(inputDTO);

        // Assert
        assertNotNull(result);
        assertEquals("New Product", result.getName());
        verify(productRepository).save(productEntity);
    }
}