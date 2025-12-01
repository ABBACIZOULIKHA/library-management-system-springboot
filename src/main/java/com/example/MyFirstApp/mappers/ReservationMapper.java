package com.example.MyFirstApp.mappers;

import com.example.MyFirstApp.dtos.ReservationDto;
import com.example.MyFirstApp.entities.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

//    @Mapping(target = "reservedAt", source = "reservationDate")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "bookId", source = "book.id")
    ReservationDto toDto(Reservation reservation);
}



