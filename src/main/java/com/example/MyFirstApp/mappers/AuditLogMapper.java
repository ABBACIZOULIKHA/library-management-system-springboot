package com.example.MyFirstApp.mappers;

import com.example.MyFirstApp.dtos.AuditLogDto;
import com.example.MyFirstApp.entities.AuditLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuditLogMapper {

    @Mapping(target = "userId", source = "user.id")
    AuditLogDto toDto(AuditLog auditLog);
}
