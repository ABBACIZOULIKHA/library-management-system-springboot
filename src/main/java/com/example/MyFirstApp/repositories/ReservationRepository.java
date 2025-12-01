package com.example.MyFirstApp.repositories;

import com.example.MyFirstApp.entities.Reservation;
import com.example.MyFirstApp.entities.enums.ReservationStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @EntityGraph(attributePaths = {"user", "book"})
    List<Reservation> findByStatus(ReservationStatus status);

    @EntityGraph(attributePaths = {"user", "book"})
    List<Reservation> findByUserId(Long userId);

    @EntityGraph(attributePaths = {"user", "book"})
    @Query("SELECT r FROM Reservation r")
    List<Reservation> findAllWithUserAndBook();
}
