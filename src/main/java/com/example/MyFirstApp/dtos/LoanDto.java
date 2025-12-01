package com.example.MyFirstApp.dtos;

import lombok.Data;
import java.time.LocalDate;

@Data
public class LoanDto {

    private Long id;
    private LocalDate borrowedAt;
    private LocalDate dueDate;
    private LocalDate returnedAt;
    private String status;
    private Long userId;
    private Long bookId;
}
