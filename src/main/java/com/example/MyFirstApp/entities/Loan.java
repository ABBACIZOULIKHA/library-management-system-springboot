package com.example.MyFirstApp.entities;

import com.example.MyFirstApp.entities.enums.LoanStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "borrowed_at")
    private LocalDate borrowedAt;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "returned_at")
    private LocalDate returnedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private LoanStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @OneToOne(mappedBy = "loan", cascade = CascadeType.ALL)
    private Fine fine;
}
