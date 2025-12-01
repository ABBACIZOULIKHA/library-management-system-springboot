package com.example.MyFirstApp.repositories;

import com.example.MyFirstApp.entities.Category;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @EntityGraph(attributePaths = "books")
    @Query("SELECT c FROM Category c")
    List<Category> findAllWithBooks();
}
