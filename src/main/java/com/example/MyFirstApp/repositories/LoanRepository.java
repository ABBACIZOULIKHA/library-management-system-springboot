package com.example.MyFirstApp.repositories;

import com.example.MyFirstApp.entities.Loan;
import com.example.MyFirstApp.entities.enums.LoanStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    @EntityGraph(attributePaths = {"user", "book"})
    List<Loan> findByUserId(Long userId);

    @EntityGraph(attributePaths = {"book"})
    List<Loan> findByStatus(LoanStatus status);

    @EntityGraph(attributePaths = {"user"})
    List<Loan> findByBookId(Long bookId);

    @EntityGraph(attributePaths = {"user", "book"})
    @Query("SELECT l FROM Loan l")
    List<Loan> findAllWithUserAndBook();
}
