package com.example.MyFirstApp.dtos;

import lombok.Data;

@Data
public class ReviewDto {

    private Long id;
    private Integer rating;
    private String comment;
    private Long userId;
    private Long bookId;
}
