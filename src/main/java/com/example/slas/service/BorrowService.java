package com.example.slas.service ;

import com.example.slas.dto.BorrowRequest ;
import com.example.slas.dto.ReturnRequest ;
import com.example.slas.dto.BorrowHistoryResponse ;
import com.example.slas.model.Book ;
import com.example.slas.model.Borrowing ;
import com.example.slas.model.User ;
import com.example.slas.enums.BookStatus ;
import com.example.slas.repository.BookRepository ;
import com.example.slas.repository.BorrowingRepository ;
import com.example.slas.repository.UserRepository ;
import org.springframework.stereotype.Service ;
import org.springframework.transaction.annotation.Transactional ;

import java.util.List ;
import java.util.ArrayList ;
import java.time.LocalDateTime;

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

        // CASUS KOD:
        System.out.println("GELEN İSTEK -> ID: " + request.getBookId());
        
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
        borrowing.setBorrowDate(LocalDateTime.now()) ;
        borrowing.setReturnDate(LocalDateTime.now().plusMinutes(1)) ; // Due in 15 days

        borrowingRepository.save(borrowing);

        // Decrease book stock
        book.setStock(book.getStock() - 1) ;

        // Update status if stock is 0
        if (book.getStock() == 0) {
            book.setStatus(BookStatus.BORROWED) ;
        }

        bookRepository.save(book) ;

    }

    @Transactional
    public void returnBook (ReturnRequest request, String userEmail) {
        System.out.println("RETURN PROCESS STARTED") ;
        
        // Find user
        User user = userRepository.findByEmail(userEmail)
                 .orElseThrow(() -> new RuntimeException("User not found")) ;
        System.out.println("1. User has been found: " + user.getEmail() + " (ID: " + user.getId() + ")") ;

        // Find book
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found")) ;
        System.out.println("2. Book has been found: " + book.getTitle() + " (ID: " + book.getId() + ")") ;

        // Database control
        System.out.println("3. Repository Query Running") ;
        System.out.println("   Searched User ID: " + user.getId()) ;
        System.out.println("   Searched Book ID: " + book.getId()) ;
        
        // Error thrower
        Borrowing borrowing = borrowingRepository.findBorrowingByIds(user.getId(), book.getId())
                .orElseThrow(() -> {
                    System.out.println("ERROR: Record not found") ;
                    System.out.println("There is no record with this User ID, Book ID and actualReturnDate = null") ;
                    return new RuntimeException("You don't have any record of borrowing this book") ;
                }) ;

        System.out.println("4. Borrowed Book ID: " + borrowing.getId()) ;
        System.out.println("   Borrowing Date: " + borrowing.getBorrowDate()) ;

        // Calculate return date
        borrowing.setActualReturnDate(LocalDateTime.now()) ;
        borrowingRepository.save(borrowing) ;
        System.out.println("5. Return date saved") ;

        // Update stock
        book.setStock(book.getStock() + 1) ;
        book.setStatus(BookStatus.AVAILABLE) ;
        bookRepository.save(book);
        System.out.println("PROCESS SUCCESSFULLY FINISHED") ;
        
    }

    // Kullanıcının ödünç geçmişini getirir
    public List<BorrowHistoryResponse> getMyHistory (String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found")) ;

        List<Borrowing> borrowings = borrowingRepository.findByUser(user);
        
        List<BorrowHistoryResponse> historyList = new ArrayList<>();

        for (Borrowing b : borrowings) {
            
            BorrowHistoryResponse dto = new BorrowHistoryResponse() ;
            dto.setId(b.getId()) ;
            dto.setBookId(b.getBook().getId());
            dto.setBookTitle(b.getBook().getTitle()) ;
            dto.setAuthor(b.getBook().getAuthor()) ;
            dto.setBorrowDate(b.getBorrowDate()) ;
            dto.setReturnDate(b.getReturnDate()) ;
            dto.setActualReturnDate(b.getActualReturnDate()) ;
            dto.setPenaltyAmount(b.getPenaltyAmount()) ;
            
            // If the book is returned and there's a fine, book considered as late
            boolean isLate = (b.getActualReturnDate() == null && LocalDateTime.now().isAfter(b.getReturnDate())) 
                             || (b.getPenaltyAmount() != null && b.getPenaltyAmount() > 0) ;
            
            dto.setLate(isLate) ;
            
            historyList.add(dto) ;
        }
        
        return historyList ;

    }

    @Transactional
    public void payAllDebts(String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found")) ;

        List<Borrowing> debts = borrowingRepository.findAllDebtsByUserId(user.getId()) ;

        for (Borrowing borrowing : debts) {
            borrowing.setPenaltyAmount(0.0) ;
        }

        borrowingRepository.saveAll(debts) ;

    }

}