package com.example.MyFirstApp.mappers;

import com.example.MyFirstApp.dtos.ReviewDto;
import com.example.MyFirstApp.entities.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "bookId", source = "book.id")
    ReviewDto toDto(Review review);
}
