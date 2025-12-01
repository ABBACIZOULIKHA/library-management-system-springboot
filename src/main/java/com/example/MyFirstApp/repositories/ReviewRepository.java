package com.example.MyFirstApp.repositories;

import com.example.MyFirstApp.entities.Review;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @EntityGraph(attributePaths = {"user", "book"})
    List<Review> findByBookId(Long bookId);

    @EntityGraph(attributePaths = {"user", "book"})
    @Query("SELECT r FROM Review r")
    List<Review> findAllWithUserAndBook();
}
