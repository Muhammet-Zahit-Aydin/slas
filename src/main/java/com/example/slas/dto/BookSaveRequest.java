package com.example.slas.dto;

import lombok.Data;

@Data
public class BookSaveRequest {

    private String title ;
    private String author ;
    private String isbn ;
    private Integer pageCount ;
    // Status will be set to AVAILABLE by default when saving a new book

}