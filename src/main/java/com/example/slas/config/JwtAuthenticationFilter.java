package com.example.slas.config ;

import com.example.slas.service.JwtService ;
import jakarta.servlet.FilterChain ;
import jakarta.servlet.ServletException ;
import jakarta.servlet.http.HttpServletRequest ;
import jakarta.servlet.http.HttpServletResponse ;
import lombok.NonNull ;
import lombok.RequiredArgsConstructor ;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken ;
import org.springframework.security.core.context.SecurityContextHolder ;
import org.springframework.security.core.userdetails.UserDetails ;
import org.springframework.security.core.userdetails.UserDetailsService ;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource ;
import org.springframework.stereotype.Component ;
import org.springframework.web.filter.OncePerRequestFilter ;

import java.io.IOException ;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService ;
    private final UserDetailsService userDetailsService ;

    @Override
    protected void doFilterInternal (@NonNull HttpServletRequest request , @NonNull HttpServletResponse response , @NonNull FilterChain filterChain) throws ServletException, IOException {

        // Take authorization from header
        final String authHeader = request.getHeader("Authorization") ;
        final String jwt ;
        final String userEmail ;

        // If there is no token or it does not start with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response) ;
            return ;

        }

        // Cut Bearer from the token and take the rest
        jwt = authHeader.substring(7) ;
        userEmail = jwtService.extractUsername(jwt) ;

        // Check if token is valid and user is not authenticated
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            // Find user in database
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail) ;

            // If the token is valit let user access the system
            if (jwtService.isTokenValid(jwt, userDetails)) {
                
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails , null , userDetails.getAuthorities()) ;
                
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // Save it in system
                SecurityContextHolder.getContext().setAuthentication(authToken) ;

            }

        }
        
        // Continue the filter chain
        filterChain.doFilter(request, response) ;

    }

}