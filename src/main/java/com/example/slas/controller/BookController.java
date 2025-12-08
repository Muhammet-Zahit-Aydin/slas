package com.example.slas.controller;

import com.example.slas.model.Book;
import com.example.slas.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/book")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    // ✔ 1) GET: Tüm kitaplar
    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // ✔ 2) POST: Yeni kitap ekle
    @PostMapping
    public Book createBook(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    // ➕ 3) GET by ID: Tek kitap getir
    @GetMapping("/{id}")
    public Optional<Book> getBookById(@PathVariable int id) {
        return bookRepository.findById(id);
    }

    // ➕ 4) PUT: Kitap güncelle
    @PutMapping("/{id}")
    public Book updateBook(@PathVariable int id, @RequestBody Book bookDetails) {
        Book book = bookRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Book not found with id: " + id)
        );

        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setGenre(bookDetails.getGenre());
        book.setIsbn(bookDetails.getIsbn());
        book.setPrice(bookDetails.getPrice());

        return bookRepository.save(book);
    }

    // ➕ 5) DELETE: Kitap sil
    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable int id) {
        bookRepository.deleteById(id);
        return "Book deleted with id: " + id;
    }
}