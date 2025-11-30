package com.example.MyFirstApp.mappers;


import com.example.MyFirstApp.dtos.RegisterUserRequest;
import com.example.MyFirstApp.dtos.UpdateUserRequest;
import com.example.MyFirstApp.dtos.UserDto;
import com.example.MyFirstApp.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
//    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    UserDto toDto(User user);
    User toEntity(RegisterUserRequest request);

    void update(UpdateUserRequest request, @MappingTarget User user);

}
