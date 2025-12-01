package com.example.MyFirstApp.controllers;

import com.example.MyFirstApp.dtos.ReservationDto;
import com.example.MyFirstApp.entities.Reservation;
import com.example.MyFirstApp.mappers.ReservationMapper;
import com.example.MyFirstApp.repositories.ReservationRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;

    @GetMapping
    public List<ReservationDto> getAll() {
        return reservationRepository.findAll()
                .stream()
                .map(reservationMapper::toDto)
                .toList();
    }

    @PostMapping
    public ResponseEntity<ReservationDto> create(@RequestBody Reservation reservation) {
        reservationRepository.save(reservation);
        return ResponseEntity.ok(reservationMapper.toDto(reservation));
    }
}
