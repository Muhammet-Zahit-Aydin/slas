package com.example.slas.dto ;
import lombok.Data ;

@Data
public class ReturnRequest {

    private Long bookId ;

    public Long getBookId () {

        return this.bookId ;

    }
    public void setBookId (Long bookId) {

        this.bookId = bookId ;

    }

}

