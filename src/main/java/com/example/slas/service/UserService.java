package com.example.slas.service ;

import com.example.slas.dto.UserResponse ;
import com.example.slas.model.User ;
import com.example.slas.repository.UserRepository ;
import org.springframework.stereotype.Service ;

import java.util.List ;
import java.util.stream.Collectors ;

@Service
public class UserService {

    private final UserRepository userRepository ;

    public UserService (UserRepository userRepository) {

        this.userRepository = userRepository ;

    }

    // Gets all users (Entity --> DTO)
    public List<UserResponse> getAllUsers () {

        return userRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList()) ;

    }

    // Gets user by email
    public UserResponse getUserByEmail (String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found")) ;
        return mapToDto(user) ;

    }
    
    // Delete user by id
    public void deleteUser (Long id) {

        userRepository.deleteById(id) ;

    }

    // Turns user entity to DTO
    private UserResponse mapToDto(User user) {

        UserResponse response = new UserResponse() ;
        response.setId(user.getId()) ;
        response.setName(user.getName()) ;
        response.setSurname(user.getSurname()) ;
        response.setEmail(user.getEmail()) ;
        response.setRole(user.getRole()) ;
        return response ;

    }
    
}