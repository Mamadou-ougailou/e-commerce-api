package com.ecommerce.product.mapper;

import com.ecommerce.product.dto.ProductDTO;
import com.ecommerce.product.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "featured", target = "isFeatured")
    @Mapping(source = "active", target = "isActive")
    ProductDTO toDTO(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(source = "isFeatured", target = "featured")
    @Mapping(source = "isActive", target = "active")
    Product toEntity(ProductDTO productDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(source = "isFeatured", target = "featured")
    @Mapping(source = "isActive", target = "active")
    void updateEntityFromDTO(@MappingTarget Product product, ProductDTO productDTO);
}
