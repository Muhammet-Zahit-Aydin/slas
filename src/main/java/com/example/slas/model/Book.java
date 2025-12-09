package com.example.slas.model;

import jakarta.persistence.*;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;
    private String title ;
    private String author ;
    private String genre ;
    private String isbn ;
    private Integer publishedYear ;
    private Double price ;

    // Constructors
    public Book () {}

    public Book (String title , String author , String genre , String isbn , Integer publishedYear , Double price) {

        this.title = title ;
        this.author = author ;
        this.genre = genre ;
        this.isbn = isbn ;
        this.publishedYear = publishedYear ;
        this.price = price ;

    }

    // Getter methods for all attributes
    public Integer getId() {
        return id;
    }
    public String getTitle() {
        return title ;
    }
    public String getAuthor() {
        return author ;
    }
    public String getGenre() {
        return genre ;
    }
    public String getIsbn() {
        return isbn ;
    }
    public Integer getPublishedYear() {
        return publishedYear ;
    }
    public Double getPrice() {
        return price ;
    }

    // Setter methods for all attributes
    public void setTitle(String title) {
        this.title = title ;
    }
    public void setAuthor(String author) {
        this.author = author ;
    }
    public void setGenre(String genre) {
        this.genre = genre ;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn ;
    }
    public void setPublishedYear(Integer publishedYear) {
        this.publishedYear = publishedYear ;
    }
    public void setPrice(Double price) {
        this.price = price ;
    }

}