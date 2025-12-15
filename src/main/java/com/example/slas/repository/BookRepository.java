package com.example.slas.repository ;

import com.example.slas.model.Book ;
import org.springframework.data.jpa.repository.JpaRepository ;
import org.springframework.data.jpa.repository.Query ;
import org.springframework.data.repository.query.Param ;

import java.util.List ;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b WHERE " +
           "LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.author) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "b.isbn LIKE CONCAT('%', :keyword, '%')")
    List<Book> searchBooks (@Param("keyword") String keyword) ;

}