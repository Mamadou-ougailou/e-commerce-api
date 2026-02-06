package com.ecommerce;

import com.ecommerce.controller.ProductController;
import com.ecommerce.dto.ProductDTO;
import com.ecommerce.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

// Static imports for cleaner code
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// 1. Use WebMvcTest to focus ONLY on the Controller layer
@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // 2. Use @MockBean to create a fake Service.
    // This allows 'when(...).thenReturn(...)' to work.
    @MockitoBean
    private ProductService productService;

    @Test
    void shouldGetAllProducts_AndReturn200() throws Exception {
        // Arrange
        Page<ProductDTO> page = new PageImpl<>(Collections.emptyList());

        // Matches: getAllProducts(int page, int size, String sortBy)
        when(productService.getAllProducts(anyInt(), anyInt(), anyString())).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/api/public/products")
                .param("page", "0")
                .param("size", "10")
                .param("sortBy", "id"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void shouldGetProductById_AndReturn200() throws Exception {
        // Arrange
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setName("Test Product");

        when(productService.getProductById(1L)).thenReturn(productDTO);

        // Act & Assert
        mockMvc.perform(get("/api/public/products/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldSearchProducts_AndReturn200() throws Exception {
        // Arrange
        Page<ProductDTO> page = new PageImpl<>(Collections.emptyList());

        // Ensure this matches your Service method signature exactly!
        // Assuming: searchProducts(String cat, Double min, Double max, String query,
        // Pageable p)
        when(productService.searchProducts(any(), any(), any(), any(), any(Pageable.class)))
                .thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/api/public/products/search")
                .param("query", "laptop")
                .param("minPrice", "100")
                .param("maxPrice", "2000"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void shouldGetFeaturedProducts_AndReturn200() throws Exception {
        // Arrange
        ProductDTO featuredProd = new ProductDTO();
        featuredProd.setName("Featured Item");

        // Note: This returns a List, not a Page (based on your earlier code)
        when(productService.getFeaturedProducts()).thenReturn(List.of(featuredProd));

        // Act & Assert
        mockMvc.perform(get("/api/public/products/featured"))
                .andExpect(status().isOk())
                // Verify the response is an Array and has the item
                .andExpect(jsonPath("$[0].name").value("Featured Item"));
    }
}