package com.example.slas.dto ;

import com.example.slas.enums.BookStatus ;
import lombok.Data ;

@Data
public class BookResponse {

    private Long id ;
    private String title ;
    private String author ;
    private String isbn ;
    private Integer pageCount ;
    private Integer stock ;
    private BookStatus status; 

}