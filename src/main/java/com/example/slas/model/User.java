package com.example.slas.model ;

import com.example.slas.enums.Role;
import jakarta.persistence.* ;
import lombok.Data ;

@Entity
@Table(name = "library_users") //! Changing table name because "user" is a forbidden keyword in MSSQL
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    // Making email unique to prevent duplicate registrations
    @Column(unique = true, nullable = false)
    private String email ;

    @Column(nullable = false)
    private String password ; // Will hold hashed password

    private String name ;
    private String surname ;

    @Enumerated(EnumType.STRING) // Putting enum as a string in database
    private Role role ;

}