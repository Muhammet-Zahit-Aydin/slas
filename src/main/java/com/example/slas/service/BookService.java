package com.example.slas.service;

import com.example.slas.model.Book;
import com.example.slas.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository repo ;

    public BookService(BookRepository repo) {
        this.repo = repo ;
    }

    public List<Book> getAll() {
        return repo.findAll() ;
    }

    public Book getById(Integer id) {
        return repo.findById(id).orElse(null) ;
    }

    public Book save(Book p) {
        return repo.save(p) ;
    }

    public void delete(Integer id) {
        repo.deleteById(id) ;
    }
}