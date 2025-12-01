package com.example.MyFirstApp.mappers;

import com.example.MyFirstApp.dtos.LoanDto;
import com.example.MyFirstApp.entities.Loan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LoanMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "status", source = "status")
    LoanDto toDto(Loan loan);
}
