package com.example.slas.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/user/test")
    public String userArea() {
        return "User area - user or admin can see this";
    }
}