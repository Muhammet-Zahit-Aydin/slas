package com.example.slas.service ;

import com.example.slas.dto.RegisterRequest ;
import com.example.slas.dto.AuthResponse ;
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

    private final EmailService emailService ;

    public AuthService (UserRepository userRepository , PasswordEncoder passwordEncoder ,AuthenticationManager authenticationManager , JwtService jwtService , EmailService emailService) {

        this.userRepository = userRepository ;
        this.passwordEncoder = passwordEncoder ;
        this.authenticationManager = authenticationManager ;
        this.jwtService = jwtService ;
        this.emailService = emailService ;

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

        String code = String.valueOf((int)(Math.random() * 9000) + 1000); 
        
        emailService.sendEmail(

            request.getEmail(),
            "Library Registiration Approval",
            "Welcome " + request.getName() + "!\n\nRegister process successfull\nValidation Code: " + code

        ) ;

        return "User successfully registered" ;

    }

    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(

            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())

        ) ;

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found")) ;

        String jwtToken = jwtService.generateToken(request.getEmail());

        return new AuthResponse(

            jwtToken , user.getRole().name() 

        ) ;
        
    }

}