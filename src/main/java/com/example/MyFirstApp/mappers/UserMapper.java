package com.example.MyFirstApp.mappers;

import com.example.MyFirstApp.dtos.RegisterUserRequest;
import com.example.MyFirstApp.dtos.UpdateUserRequest;
import com.example.MyFirstApp.dtos.UserDto;
import com.example.MyFirstApp.entities.User;
import com.example.MyFirstApp.entities.enums.Role;
import com.example.MyFirstApp.entities.enums.UserStatus;
import org.mapstruct.*;

@Mapper(componentModel = "spring", imports = {Role.class, UserStatus.class})
public interface UserMapper {

    UserDto toDto(User user);

    @Mapping(target = "role", expression = "java(Role.valueOf(request.getRole()))")
    @Mapping(target = "status", expression = "java(UserStatus.ACTIVE)")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    User toEntity(RegisterUserRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "loans", ignore = true)
    @Mapping(target = "reservations", ignore = true)
    @Mapping(target = "logs", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "status", expression = "java(request.getStatus() != null ? UserStatus.valueOf(request.getStatus()) : user.getStatus())")
    void update(UpdateUserRequest request, @MappingTarget User user);
}