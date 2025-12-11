package com.example.slas.service ;

import com.example.slas.dto.BorrowRequest ;
import com.example.slas.dto.ReturnRequest ;
import com.example.slas.model.Book ;
import com.example.slas.model.Borrowing ;
import com.example.slas.model.User ;
import com.example.slas.enums.BookStatus ;
import com.example.slas.repository.BookRepository ;
import com.example.slas.repository.BorrowingRepository ;
import com.example.slas.repository.UserRepository ;
import org.springframework.stereotype.Service ;
import org.springframework.transaction.annotation.Transactional ;

import java.time.LocalDate ;

@Service
public class BorrowService {

    private final BorrowingRepository borrowingRepository ;
    private final BookRepository bookRepository ;
    private final UserRepository userRepository ;

    public BorrowService (BorrowingRepository borrowingRepository , BookRepository bookRepository , UserRepository userRepository) {

        this.borrowingRepository = borrowingRepository ;
        this.bookRepository = bookRepository ;
        this.userRepository = userRepository ;

    }

    @Transactional // If there's an error, rollback all operations
    public void borrowBook(BorrowRequest request, String userEmail) {
        // Find user
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found")) ;

        // Find book
        Book book = bookRepository.findById(request.getBookId()).orElseThrow(() -> new RuntimeException("Book not found")) ;

        // Check if book is available
        if (book.getStatus() == BookStatus.BORROWED) {

            throw new RuntimeException("This book isn't available for borrowing") ;

        }

        // Make borrowing record
        Borrowing borrowing = new Borrowing() ;
        borrowing.setUser(user) ;
        borrowing.setBook(book) ;
        borrowing.setBorrowDate(LocalDate.now()) ;
        borrowing.setReturnDate(LocalDate.now().plusDays(15)) ; // Due in 15 days

        borrowingRepository.save(borrowing) ;

        // Update book status
        book.setStatus(BookStatus.BORROWED) ;
        bookRepository.save(book) ;

    }

    @Transactional
    public void returnBook (ReturnRequest request) {
        // Find book
        Book book = bookRepository.findById(request.getBookId()).orElseThrow(() -> new RuntimeException("Book not found")); ;

        // Check if book is already available
        if (book.getStatus() == BookStatus.AVAILABLE) {
            throw new RuntimeException("This book have never been borrowed");
        }

        // Find related borrowing record
        Borrowing borrowing = borrowingRepository.findByBookAndActualReturnDateIsNull(book)
                .orElseThrow(() -> new RuntimeException("No active borrowing record found for this book")) ;

        // Make return date today
        borrowing.setActualReturnDate(LocalDate.now()) ;
        borrowingRepository.save(borrowing) ;

        // Make book available again
        book.setStatus(BookStatus.AVAILABLE) ;
        bookRepository.save(book) ;

    }

}