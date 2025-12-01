package com.example.MyFirstApp.mappers;

import com.example.MyFirstApp.dtos.BookDto;
import com.example.MyFirstApp.entities.Author;
import com.example.MyFirstApp.entities.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "authorIds", expression = "java(mapAuthors(book.getAuthors()))")
    BookDto toDto(Book book);

    default List<Long> mapAuthors(List<Author> authors) {
        if (authors == null) return null;
        return authors.stream()
                .map(Author::getId)
                .collect(Collectors.toList());
    }
}
