package com.example.MyFirstApp.repositories;

import com.example.MyFirstApp.entities.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @EntityGraph(attributePaths = {"category", "authors"})
    List<Book> findByCategoryId(Long categoryId);

    @EntityGraph(attributePaths = {"category", "authors"})
    @Query("SELECT b FROM Book b")
    List<Book> findAllWithCategoryAndAuthors();

    @EntityGraph(attributePaths = {"authors"})
    Optional<Book> findByIsbn(String isbn);

    @EntityGraph(attributePaths = {"category"})
    List<Book> findByTitleContainingIgnoreCase(String keyword);
}
