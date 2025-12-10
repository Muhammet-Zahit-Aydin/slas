package com.example.slas.config ;

import com.example.slas.service.CustomUserDetailsService ;
import org.springframework.context.annotation.Bean ;
import org.springframework.context.annotation.Configuration ;
import org.springframework.security.config.annotation.web.builders.HttpSecurity ;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder ;
import org.springframework.security.crypto.password.PasswordEncoder ;
import org.springframework.security.web.SecurityFilterChain ;
import org.springframework.security.authentication.AuthenticationManager ;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration ;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider ;
import org.springframework.security.authentication.AuthenticationProvider ;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService ;
    
    public SecurityConfig (CustomUserDetailsService userDetailsService) {

        this.userDetailsService = userDetailsService ;

    }

    // Crypter Bean
    @Bean
    public PasswordEncoder passwordEncoder () {

        return new BCryptPasswordEncoder() ;

    }

    // Firewall settings
    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Making "/register" and "/login" adresses available for everyone
                .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()
                // Making rest locked
                .anyRequest().authenticated()
            ) ;
            
        return http.build() ;
        
    }

    // Authentication Manager Bean
    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager() ;

    }

    // Function telling Spring Security how to find users and how to encode/decode passwords
    @Bean
    public AuthenticationProvider authenticationProvider () {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider() ;
        authProvider.setUserDetailsService(userDetailsService) ;
        authProvider.setPasswordEncoder(passwordEncoder()) ;
        return authProvider ;
        
    }

}