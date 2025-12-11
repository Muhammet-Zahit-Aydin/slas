package com.example.slas.dto ;

import lombok.Data ;
import java.time.LocalDate ;

@Data
public class BorrowHistoryResponse {

    private Long id ;
    private String bookTitle ;
    private String author ;
    private LocalDate borrowDate ;
    private LocalDate returnDate ;
    private LocalDate actualReturnDate ; 
    private Double penalty ;
    private boolean isLate ;

}