package com.example.slas.scheduler ;

import com.example.slas.model.Borrowing ;
import com.example.slas.repository.BorrowingRepository ;
import com.example.slas.service.EmailService ;
import lombok.RequiredArgsConstructor ;
import org.springframework.scheduling.annotation.Scheduled ;
import org.springframework.stereotype.Component ;

import java.time.LocalDateTime ;
import java.util.List ;

@Component
@RequiredArgsConstructor
public class OverdueScheduler {

    private final BorrowingRepository borrowingRepository ;
    private final EmailService emailService ;

    @Scheduled(fixedRate = 60000) 
    public void checkOverdueBooks() {
        System.out.println("Automated control working...") ;

        LocalDateTime now = LocalDateTime.now() ;

        List<Borrowing> overdueList = borrowingRepository.findOverdueBooks(now) ;

        for (Borrowing b : overdueList) {
            String userEmail = b.getUser().getEmail() ;
            String bookTitle = b.getBook().getTitle() ;

            System.out.println("Latency found: " + userEmail) ;

            emailService.sendEmail(

                userEmail,
                "İade Süresi Geçti!",
                "Sayın üyemiz,\n\n'" + bookTitle + "' isimli kitabın iade tarihi geçmiştir.\nLütfen en kısa sürede iade ediniz, aksi takdirde ceza işleyecektir."

            );
        }
    }
}