package com.example.slas.service ;

import com.example.slas.dto.RegisterRequest ;
import com.example.slas.model.User ;
import com.example.slas.repository.UserRepository ;
import org.springframework.security.crypto.password.PasswordEncoder ;
import org.springframework.stereotype.Service ;
import org.springframework.security.authentication.AuthenticationManager ;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken ;
import com.example.slas.dto.LoginRequest ;

@Service
public class AuthService {

    private final UserRepository userRepository ;
    private final PasswordEncoder passwordEncoder ;
    
    private final AuthenticationManager authenticationManager ;
    private final JwtService jwtService ;

    public AuthService (UserRepository userRepository , PasswordEncoder passwordEncoder ,AuthenticationManager authenticationManager , JwtService jwtService) {

        this.userRepository = userRepository ;
        this.passwordEncoder = passwordEncoder ;
        this.authenticationManager = authenticationManager ;
        this.jwtService = jwtService ;

    }

    public String register (RegisterRequest request) {

        // Check if email is already taken
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {

            throw new RuntimeException("This e-mail is already in use") ;
            
        }

        // Make a new user
        User user = new User() ;
        user.setName(request.getName()) ;
        user.setSurname(request.getSurname()) ;
        user.setEmail(request.getEmail()) ;
        user.setRole(request.getRole()) ;

        // Hash the password before saving
        user.setPassword(passwordEncoder.encode(request.getPassword())) ;

        // Save the user to database
        userRepository.save(user) ;

        return "User successfully registered." ;

    }

    public String login (LoginRequest request) {

        // Spring Security checking password
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail() , request.getPassword())) ;

        return jwtService.generateToken(request.getEmail()) ;
    }

}