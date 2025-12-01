package com.example.MyFirstApp.dtos;


import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AuditLogDto {

    private Long id;
    private String action;
    private LocalDateTime timestamp;
    private Long userId;
}
