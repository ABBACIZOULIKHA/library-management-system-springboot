package com.example.MyFirstApp.mappers;

import com.example.MyFirstApp.dtos.CategoryDto;
import com.example.MyFirstApp.entities.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDto toDto(Category category);
}
