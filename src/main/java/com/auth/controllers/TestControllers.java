package com.auth.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestControllers {
    
    @GetMapping
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello Test Controller...");
    }

    @GetMapping("/get")
    public ResponseEntity<String> get() {
        return ResponseEntity.ok("GET - Controller");
    }

    @PostMapping("/post")
    public ResponseEntity<String> post() {
        return ResponseEntity.ok("POST - Controller");
    }

    @PutMapping("/put")
    public ResponseEntity<String> put() {
        return ResponseEntity.ok("PUT - Controller");
    }
}
