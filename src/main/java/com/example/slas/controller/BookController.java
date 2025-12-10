package com.example.slas.controller ;

import com.example.slas.dto.BookResponse ;
import com.example.slas.dto.BookSaveRequest ;
import com.example.slas.service.BookService ;
import org.springframework.http.HttpStatus ;
import org.springframework.http.ResponseEntity ;
import org.springframework.security.access.prepost.PreAuthorize ;
import org.springframework.web.bind.annotation.* ;

import java.util.List ;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService ;

    public BookController (BookService bookService) {

        this.bookService = bookService ;

    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks () {

        return ResponseEntity.ok(bookService.getAllBooks()) ;

    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById (@PathVariable Long id) {

        return ResponseEntity.ok(bookService.getBookById(id)) ;

    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<BookResponse> saveBook (@RequestBody BookSaveRequest request) {
        
        BookResponse response = bookService.createBook(request) ;
        return ResponseEntity.status(HttpStatus.CREATED).body(response) ;

    }
}