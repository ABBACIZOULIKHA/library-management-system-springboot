package com.example.MyFirstApp.mappers;

import com.example.MyFirstApp.dtos.PublisherDto;
import com.example.MyFirstApp.entities.Publisher;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PublisherMapper {

    PublisherDto toDto(Publisher publisher);
}
