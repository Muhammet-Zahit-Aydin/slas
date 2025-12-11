package com.example.slas.exception ;

import org.springframework.http.HttpStatus ;
import org.springframework.http.ResponseEntity ;
import org.springframework.web.bind.annotation.ExceptionHandler ;
import org.springframework.web.bind.annotation.RestControllerAdvice ;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Catches "This book isn't available for borrowing" runtime exceptions
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException (RuntimeException e) {

        // Return 400 Bad Request
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()) ;

    }

}