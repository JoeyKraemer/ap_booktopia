package com.nhlstenden.booktopia.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*") // For development
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Welcome to Booktopia!";
    }
}