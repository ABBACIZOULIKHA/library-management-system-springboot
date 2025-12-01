package com.example.MyFirstApp.mappers;


import com.example.MyFirstApp.dtos.AuthorDto;
import com.example.MyFirstApp.entities.Author;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    AuthorDto toDto(Author author);
}
