package com.example.MyFirstApp.controllers;


import com.example.MyFirstApp.dtos.LoanDto;
import com.example.MyFirstApp.entities.Book;
import com.example.MyFirstApp.entities.Loan;
import com.example.MyFirstApp.entities.User;
import com.example.MyFirstApp.mappers.LoanMapper;
import com.example.MyFirstApp.repositories.BookRepository;
import com.example.MyFirstApp.repositories.LoanRepository;
import com.example.MyFirstApp.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/loans")
public class LoanController {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final LoanMapper loanMapper;

    @GetMapping
    public List<LoanDto> getAll() {
        return loanRepository.findAll()
                .stream()
                .map(loanMapper::toDto)
                .toList();
    }

    @PostMapping
    public ResponseEntity<LoanDto> create(@RequestBody Loan loan) {
        User user = userRepository.findById(loan.getUser().getId()).orElse(null);
        Book book = bookRepository.findById(loan.getBook().getId()).orElse(null);

        if (user == null || book == null)
            return ResponseEntity.badRequest().build();

        loan.setUser(user);
        loan.setBook(book);
        loanRepository.save(loan);

        return ResponseEntity.ok(loanMapper.toDto(loan));
    }
}
