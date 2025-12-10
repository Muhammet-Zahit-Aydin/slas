package com.example.slas.repository ;

import com.example.slas.model.User ;
import org.springframework.data.jpa.repository.JpaRepository ;
import java.util.Optional ;

public interface UserRepository extends JpaRepository<User, Long> {

    // Finds user by email for login purposes
    Optional<User> findByEmail(String email) ;

}