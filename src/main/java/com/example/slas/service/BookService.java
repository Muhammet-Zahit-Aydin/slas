package com.example.slas.service ;

import com.example.slas.dto.BookResponse ;
import com.example.slas.dto.BookSaveRequest ;
import com.example.slas.model.Book ;
import com.example.slas.enums.BookStatus ;
import com.example.slas.repository.BookRepository ;
import org.springframework.stereotype.Service ;

import java.util.List ;
import java.util.stream.Collectors ;

@Service
public class BookService {

    private final BookRepository bookRepository ;

    public BookService (BookRepository bookRepository) {

        this.bookRepository = bookRepository ;

    }

    // Add new book
    public BookResponse createBook (BookSaveRequest request) {

        Book book = new Book() ;
        book.setTitle(request.getTitle()) ;
        book.setAuthor(request.getAuthor()) ;
        book.setIsbn(request.getIsbn()) ;
        book.setPageCount(request.getPageCount()) ;
        book.setStock(request.getStock()) ;
        book.setStatus(request.getStock() > 0 ? BookStatus.AVAILABLE : BookStatus.BORROWED) ;

        Book savedBook = bookRepository.save(book) ;
        return mapToDto(savedBook) ;

    }

    // List all books
    public List<BookResponse> getAllBooks () {

        return bookRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList()) ;

    }
    
    // Get book by ID
    public BookResponse getBookById (Long id) {

        Book book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found")) ;
        return mapToDto(book) ;

    }

    // Entity to DTO mapping
    private BookResponse mapToDto(Book book) {

        BookResponse response = new BookResponse() ;
        response.setId(book.getId()) ;
        response.setTitle(book.getTitle()) ;
        response.setAuthor(book.getAuthor()) ;
        response.setIsbn(book.getIsbn()) ;
        response.setPageCount(book.getPageCount()) ;
        response.setStock(book.getStock()) ;
        response.setStatus(book.getStatus()) ; 
        
        return response ;

    }

    // Search Engine
    public List<BookResponse> searchBooks (String keyword) {
        List<Book> books ;
        
        // If search bar is empty get all
        if (keyword == null || keyword.trim().isEmpty()) {

            books = bookRepository.findAll() ;

        } else {
            
            books = bookRepository.searchBooks(keyword.trim()) ;

        }

        // Turn entity list to dto
        return books.stream().map(this::mapToDto).collect(Collectors.toList()) ;
    }

}