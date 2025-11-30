package com.example.MyFirstApp.mappers;

import com.example.MyFirstApp.dtos.ProductDto;
import com.example.MyFirstApp.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "categoryId", source = "category.id")
    ProductDto toDto(Product product);
}
