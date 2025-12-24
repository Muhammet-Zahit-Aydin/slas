package com.example.slas.controller ;

import com.example.slas.dto.UserProfileResponse;
import com.example.slas.dto.UserResponse ;
import com.example.slas.model.User ;
import com.example.slas.repository.UserRepository ;
import com.example.slas.repository.BorrowingRepository ;
import com.example.slas.service.UserService ;
import org.springframework.http.ResponseEntity ;
import org.springframework.security.core.Authentication ;
import org.springframework.security.core.context.SecurityContextHolder ;
import org.springframework.web.bind.annotation.* ;
import org.springframework.security.access.prepost.PreAuthorize ;

import java.util.List ;
import java.util.Map ;
import java.util.HashMap ;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService ;
    private final UserRepository userRepository;
    private final BorrowingRepository borrowingRepository;

    public UserController (UserService userService , UserRepository userRepository , BorrowingRepository borrowingRepository) {

        this.userService = userService ;
        this.userRepository = userRepository ;
        this.borrowingRepository = borrowingRepository ;

    }

    // List all users
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        return ResponseEntity.ok(userService.getAllUsers()) ;

    }

    // Function to view logged users profile
    // Takes whom is logged in from the token
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyProfile () {

        // Takes logged users info
        Authentication auth = SecurityContextHolder.getContext().getAuthentication() ;
        String currentEmail = auth.getName() ; // Gives email in token
        
        return ResponseEntity.ok(userService.getUserByEmail(currentEmail)) ;

    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getProfile(Authentication auth) {

        // Find logged in user
        User user = userRepository.findByEmail(auth.getName()).orElseThrow() ;

        // Calculate statistics
        int totalRead = borrowingRepository.countTotalBooks(user.getId());
        int active = borrowingRepository.countActiveBooks(user.getId());
        Double penalty = borrowingRepository.sumTotalPenalty(user.getId());

        UserProfileResponse response = new UserProfileResponse();
        response.setFullName(user.getName() + " " + user.getSurname()) ;
        response.setEmail(user.getEmail()) ;
        response.setTotalBooksRead(totalRead) ;
        response.setActiveBooks(active) ;
        response.setTotalPenalty(penalty) ;

        return ResponseEntity.ok(response) ;

    }
    
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {

        userService.deleteUser(id) ;
        return ResponseEntity.ok("User deleted.") ;

    }

    //! DEBUG METHOD - SHOWS CURRENT AUTHENTICATION DETAILS
    @GetMapping("/test-auth")
    public ResponseEntity<Object> testAuth () {
        // Print information of currently logged in user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication() ;
        
        Map<String, Object> info = new HashMap<>() ;
        info.put("User", auth.getName()) ;
        info.put("Authorities", auth.getAuthorities()) ;
        
        return ResponseEntity.ok(info) ;
    }

}