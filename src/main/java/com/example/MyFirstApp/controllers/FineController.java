package com.example.MyFirstApp.controllers;


import com.example.MyFirstApp.dtos.FineDto;
import com.example.MyFirstApp.entities.Fine;
import com.example.MyFirstApp.mappers.FineMapper;
import com.example.MyFirstApp.repositories.FineRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/fines")
public class FineController {

    private final FineRepository fineRepository;
    private final FineMapper fineMapper;

    @GetMapping
    public List<FineDto> getAll() {
        return fineRepository.findAll()
                .stream()
                .map(fineMapper::toDto)
                .toList();
    }

    @PostMapping
    public FineDto create(@RequestBody Fine fine) {
        fineRepository.save(fine);
        return fineMapper.toDto(fine);
    }
}
