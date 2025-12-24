package com.example.slas.dto ;

import lombok.Data ;

@Data
public class UserProfileResponse {

    private String fullName ;
    private String email ;
    private int totalBooksRead ;   
    private int activeBooks ;
    private double totalPenalty ;

}