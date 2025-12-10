package com.example.slas.dto ;

import lombok.Data ;
import com.example.slas.enums.Role ;

@Data
public class RegisterRequest {

    private String name ;
    private String surname ;
    private String email ;
    private String password ;
    private Role role ;

}