package com.example.slas.model ;

import jakarta.persistence.* ;  
import lombok.Data ;
import java.time.LocalDate ;

@Entity
@Table(name = "borrowings")
@Data
public class Borrowing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    // Borrowing user
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user ;

    // Borrowed book
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book ;

    // When was the book borrowed?
    private LocalDate borrowDate ;

    // When is the book due to be returned?
    private LocalDate returnDate ;

    // When was the book actually returned?
    private LocalDate actualReturnDate ;
    
}