package com.example.slas.controller ;

import com.example.slas.dto.BorrowRequest ;
import com.example.slas.dto.ReturnRequest;
import com.example.slas.service.BorrowService ;
import org.springframework.http.ResponseEntity ;
import org.springframework.security.core.Authentication ;
import org.springframework.security.core.context.SecurityContextHolder ;
import org.springframework.web.bind.annotation.* ;

@RestController
@RequestMapping("/api/borrow")
public class BorrowController {

    private final BorrowService borrowService ;

    public BorrowController (BorrowService borrowService) {

        this.borrowService = borrowService ;

    }

    @PostMapping
    public ResponseEntity<String> borrowBook (@RequestBody BorrowRequest request) {

        // Take current user's email
        Authentication auth = SecurityContextHolder.getContext().getAuthentication() ;
        String currentEmail = auth.getName() ;

        borrowService.borrowBook(request, currentEmail) ;

        return ResponseEntity.ok("Book borrowed successfully") ;

    }

    @PutMapping("/return")
    public ResponseEntity<String> returnBook (@RequestBody ReturnRequest request) {

        borrowService.returnBook(request) ;
        return ResponseEntity.ok("Book has been returned successfully") ;
        
    }

}