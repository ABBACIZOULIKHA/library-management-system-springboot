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

    @Mapping(target = "pdfUrl", expression = "java(buildPdfUrl(book))")
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "authorIds", expression = "java(mapAuthors(book.getAuthors()))")
    BookDto toDto(Book book);

    default List<Long> mapAuthors(List<Author> authors) {
        if (authors == null) return null;
        return authors.stream()
                .map(Author::getId)
                .collect(Collectors.toList());
    }

    default String buildPdfUrl(Book book) {
        if (book.getPdfPath() == null) return null;
        return "http://localhost:8080/books/" + book.getId() + "/pdf";
    }

}


