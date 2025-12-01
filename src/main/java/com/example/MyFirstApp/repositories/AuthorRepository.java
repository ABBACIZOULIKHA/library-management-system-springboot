package com.example.MyFirstApp.repositories;

import com.example.MyFirstApp.entities.Author;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    @EntityGraph(attributePaths = "books")
    @Query("SELECT a FROM Author a")
    List<Author> findAllWithBooks();
}

