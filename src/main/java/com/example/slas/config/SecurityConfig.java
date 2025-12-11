package com.example.slas.config ;

import com.example.slas.service.CustomUserDetailsService ;
import lombok.RequiredArgsConstructor ;
import org.springframework.context.annotation.Bean ;
import org.springframework.context.annotation.Configuration ;
import org.springframework.security.authentication.AuthenticationManager ;
import org.springframework.security.authentication.AuthenticationProvider ;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider ;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration ;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity ;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity ;
import org.springframework.security.config.http.SessionCreationPolicy ;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder ;
import org.springframework.security.crypto.password.PasswordEncoder ;
import org.springframework.security.web.SecurityFilterChain ;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter ;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter ;
    private final CustomUserDetailsService userDetailsService ;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(auth -> auth
 
                .requestMatchers(
                    "/api/auth/**",     // Login/Register
                    "/v3/api-docs/**",  // Swagger JSON data
                    "/swagger-ui/**",   // Swagger interface
                    "/swagger-ui.html"  // Swagger guidance
                ).permitAll()
                
                // Test endpoints
                .requestMatchers("/api/users/test-auth").authenticated()
                // Authentication endpoints
                .requestMatchers("/api/auth/**").permitAll()
                // Exception management
                .requestMatchers("/error").permitAll()

                .anyRequest().authenticated()
            ) ;

        return http.build() ;
    }

    @Bean
    public AuthenticationProvider authenticationProvider () {
        
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider() ;
        authProvider.setUserDetailsService(userDetailsService) ;
        authProvider.setPasswordEncoder(passwordEncoder()) ;
        return authProvider;

    }

    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration config) throws Exception {

        return config.getAuthenticationManager() ;

    }

    @Bean
    public PasswordEncoder passwordEncoder () {

        return new BCryptPasswordEncoder() ;

    }
    
}