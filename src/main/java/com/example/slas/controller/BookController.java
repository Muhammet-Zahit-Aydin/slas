package com.example.slas.controller ;

import com.example.slas.dto.AddBookRequest;
import com.example.slas.dto.BookResponse ;
import com.example.slas.service.BookService ;
import org.springframework.http.ResponseEntity ;
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

    @GetMapping("/search")
    public ResponseEntity<List<BookResponse>> searchBooks (@RequestParam(name = "query", required = false) String query) {

        return ResponseEntity.ok(bookService.searchBooks(query)) ;
        
    }

    @PostMapping("/add")
    public ResponseEntity<String> addBook(@RequestBody AddBookRequest request) {

        bookService.addBook(request) ;
        return ResponseEntity.ok("Book successfully added") ;
        
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {

        bookService.deleteBook(id) ;
        return ResponseEntity.ok("Book succcessfully deleted") ;

    }
    
}