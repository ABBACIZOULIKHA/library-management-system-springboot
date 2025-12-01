package com.example.MyFirstApp.dtos;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReservationDto {

    private Long id;
    private LocalDateTime reservedAt;
    private String status;
    private Long userId;
    private Long bookId;
}
