package com.example.slas.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    @GetMapping("/admin/test")
    public String adminArea() {
        return "Admin area - only admins can see this";
    }
}