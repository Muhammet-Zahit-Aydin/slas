package com.example.slas.dto ;

import lombok.Data ;

@Data
public class AddBookRequest {

    private String title ;
    private String author ;
    private String isbn ;
    private Integer pageCount ;
    private Integer stock ;

}