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

        // Check stock
        if (book.getStock() <= 0) {

            throw new RuntimeException("This book is out of stock") ;
        
        }

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

        // Decrease book stock
        book.setStock(book.getStock() - 1) ;

        // Update status if stock is 0
        if (book.getStock() == 0) {
            book.setStatus(BookStatus.BORROWED) ;
        }

        bookRepository.save(book) ;

    }

    @Transactional
    public void returnBook (ReturnRequest request , String userEmail) {

        // Find user
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found")) ;

        // Find book
        Book book = bookRepository.findById(request.getBookId()).orElseThrow(() -> new RuntimeException("Book not found")); ;

        // Check if book is already available
        if (book.getStatus() == BookStatus.AVAILABLE) {
            throw new RuntimeException("This book have never been borrowed") ;
        }

        // Find related borrowing record
        // Borrowing borrowing = borrowingRepository.findByUserAndBookAndActualReturnDateIsNull(user, book)
        //         .orElseThrow(() -> new RuntimeException("There is no active borrowing record for this book")) ;

        System.out.println("Sorgulaniyor -> User ID: " + user.getId() + " | Book ID: " + book.getId()) ;

        Borrowing borrowing = borrowingRepository.findBorrowingByIds(user.getId(), book.getId())
                .orElseThrow(() -> new RuntimeException("Sizde bu kitaba ait aktif bir ödünç kaydi yok! (ID eşleşmedi)")) ;

        // Make return date today
        borrowing.setActualReturnDate(LocalDate.now()) ;
        borrowingRepository.save(borrowing) ;

        // Increase book stock
        book.setStock(book.getStock() + 1) ;
        
        // Update status
        book.setStatus(BookStatus.AVAILABLE) ;
        bookRepository.save(book) ;

    }

}