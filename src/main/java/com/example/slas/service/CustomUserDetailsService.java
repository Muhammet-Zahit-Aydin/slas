package com.example.slas.service ;

import com.example.slas.model.User ;
import com.example.slas.repository.UserRepository ;
import org.springframework.security.core.authority.SimpleGrantedAuthority ;
import org.springframework.security.core.userdetails.UserDetails ;
import org.springframework.security.core.userdetails.UserDetailsService ;
import org.springframework.security.core.userdetails.UsernameNotFoundException ;
import org.springframework.stereotype.Service ;

import java.util.Collections ;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository ;

    public CustomUserDetailsService (UserRepository userRepository) {

        this.userRepository = userRepository ;

    }

    @Override
    public UserDetails loadUserByUsername (String email) throws UsernameNotFoundException {

        // Find user in database
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found: " + email)) ;

        // Translating user entity Spring Securty language
        return new org.springframework.security.core.userdetails.User(

                user.getEmail() ,
                user.getPassword() ,

                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name())) 
                
        ) ;

    }

}