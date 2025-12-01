package com.example.MyFirstApp.mappers;

import com.example.MyFirstApp.dtos.FineDto;
import com.example.MyFirstApp.entities.Fine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FineMapper {

    @Mapping(target = "loanId", source = "loan.id")
    FineDto toDto(Fine fine);
}
