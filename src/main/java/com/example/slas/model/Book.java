package com.example.slas.model ;

import com.example.slas.enums.BookStatus ;
import jakarta.persistence.* ;
import lombok.Data ;

@Entity
@Table(name = "books")
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @Column(nullable = false)
    private String title ;

    @Column(nullable = false)
    private String author ;

    @Column(unique = true)
    private String isbn ;

    private Integer pageCount;

    @Column(nullable = false)
    private Integer stock ;

    @Enumerated(EnumType.STRING)
    private BookStatus status ;

    @Column(nullable = false)
    private String category ;

}