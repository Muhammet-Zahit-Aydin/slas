package com.example.slas.dto ;

import lombok.Data ;
import java.time.LocalDateTime ;

@Data
public class BorrowHistoryResponse {

    private Long id ;
    private Long bookId ;
    private String bookTitle ;
    private String author ;
    private LocalDateTime borrowDate ;
    private LocalDateTime returnDate ;
    private LocalDateTime actualReturnDate ; 
    private Double penalty ;
    private boolean isLate ;

}