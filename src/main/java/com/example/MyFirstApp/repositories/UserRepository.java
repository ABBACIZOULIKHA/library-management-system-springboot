package com.example.MyFirstApp.repositories;

import com.example.MyFirstApp.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = {"loans", "reservations"})
    @Query("SELECT u FROM User u")
    List<User> findAllWithLoansAndReservations();

}
