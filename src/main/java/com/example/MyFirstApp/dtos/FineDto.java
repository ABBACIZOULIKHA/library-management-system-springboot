package com.example.MyFirstApp.dtos;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class FineDto {

    private Long id;
    private BigDecimal amount;
    private Boolean paid;
    private Long loanId;
}
