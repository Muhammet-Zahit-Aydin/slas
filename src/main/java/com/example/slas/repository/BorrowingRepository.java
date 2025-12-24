package com.example.slas.repository ;

import com.example.slas.model.Borrowing ;
import com.example.slas.model.User ;
import org.springframework.data.jpa.repository.JpaRepository ;
import org.springframework.data.jpa.repository.Query ;
import org.springframework.data.repository.query.Param ;

import java.time.LocalDateTime;
import java.util.List ;
import java.util.Optional ;
import com.example.slas.model.Book ;

public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {

    // Find all borrowings of user
    List<Borrowing> findByUser(User user) ;

    // SQL Version: SELECT * FROM borrowings WHERE book_id = ? AND actual_return_date IS NULL
    Optional<Borrowing> findByBookAndActualReturnDateIsNull(Book book) ;

    @Query("SELECT b FROM Borrowing b WHERE b.user.id = :userId AND b.book.id = :bookId AND b.actualReturnDate IS NULL")
    Optional<Borrowing> findBorrowingByIds(@Param("userId") Long userId, @Param("bookId") Long bookId) ;

    @Query("SELECT COUNT(b) FROM Borrowing b WHERE b.user.id = :userId")
    int countTotalBooks(Long userId);

    @Query("SELECT COUNT(b) FROM Borrowing b WHERE b.user.id = :userId AND b.actualReturnDate IS NULL")
    int countActiveBooks(Long userId);

    @Query("SELECT COALESCE(SUM(b.penaltyAmount), 0) FROM Borrowing b WHERE b.user.id = :userId")
    Double sumTotalPenalty(Long userId);

    @Query("SELECT b FROM Borrowing b WHERE b.returnDate < :now AND b.actualReturnDate IS NULL")
    List<Borrowing> findOverdueBooks(LocalDateTime now) ;

}