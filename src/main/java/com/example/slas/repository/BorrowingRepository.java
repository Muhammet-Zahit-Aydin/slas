package com.example.slas.repository ;

import com.example.slas.model.Borrowing ;
import com.example.slas.model.User ;
import org.springframework.data.jpa.repository.JpaRepository ;

import java.util.List ;
import java.util.Optional ;
import com.example.slas.model.Book ;

public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {

    // Find all borrowings of user
    List<Borrowing> findByUser(User user) ;

    // SQL Version: SELECT * FROM borrowings WHERE book_id = ? AND actual_return_date IS NULL
    Optional<Borrowing> findByBookAndActualReturnDateIsNull(Book book) ;

}