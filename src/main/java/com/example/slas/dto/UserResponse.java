package com.example.slas.dto ;

import com.example.slas.enums.Role ;
import lombok.Data ;

@Data
public class UserResponse {

    private Long id ;
    private String name ;
    private String surname ;
    private String email ;
    private Role role ;

}