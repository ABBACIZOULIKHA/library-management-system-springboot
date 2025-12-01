package com.example.MyFirstApp.dtos;

import lombok.Data;
import java.util.List;

@Data
public class BookDto {

    private Long id;
    private String title;
    private String isbn;
    private String publisher;
    private Integer publishYear;
    private Integer totalCopies;
    private Integer availableCopies;
    private String shelfLocation;
    private Long categoryId;
    private List<Long> authorIds;
}
