package com.example.MyFirstApp.controllers;

import com.example.MyFirstApp.dtos.ReviewDto;
import com.example.MyFirstApp.entities.Review;
import com.example.MyFirstApp.mappers.ReviewMapper;
import com.example.MyFirstApp.repositories.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    @GetMapping
    public List<ReviewDto> getAll() {
        return reviewRepository.findAll()
                .stream()
                .map(reviewMapper::toDto)
                .toList();
    }

    @PostMapping
    public ReviewDto create(@RequestBody Review review) {
        reviewRepository.save(review);
        return reviewMapper.toDto(review);
    }
}
