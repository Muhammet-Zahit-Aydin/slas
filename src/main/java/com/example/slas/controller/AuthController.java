package com.example.slas.controller ;

import com.example.slas.dto.LoginRequest;
import com.example.slas.dto.RegisterRequest ;
import com.example.slas.service.AuthService ;
import org.springframework.http.ResponseEntity ;
import org.springframework.web.bind.annotation.* ;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService ;

    public AuthController (AuthService authService) {

        this.authService = authService ;

    }

    @PostMapping("/register")
    public ResponseEntity<String> register (@RequestBody RegisterRequest request) {

        String result = authService.register(request) ;
        return ResponseEntity.ok(result) ;

    }

    @PostMapping("/login")
    public String login (@RequestBody LoginRequest request) {

        return authService.login(request) ;

    }

}