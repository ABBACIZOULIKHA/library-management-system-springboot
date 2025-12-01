package com.example.MyFirstApp.repositories;

import com.example.MyFirstApp.entities.Fine;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FineRepository extends JpaRepository<Fine, Long> {

    @EntityGraph(attributePaths = "loan")
    Optional<Fine> findByLoanId(Long loanId);

    @EntityGraph(attributePaths = "loan")
    @Query("SELECT f FROM Fine f")
    List<Fine> findAllWithLoan();
}
