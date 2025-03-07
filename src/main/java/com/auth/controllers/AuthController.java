package com.auth.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.controllers.DTO.AuthLoginRequest;
import com.auth.controllers.DTO.AuthRegisterUserRequest;
import com.auth.controllers.DTO.AuthResponse;
import com.auth.services.UserDetailServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserDetailServiceImpl userDetailServiceImpl;

    @PostMapping("/log-in")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest authLoginRequest) {
        return new ResponseEntity<>(this.userDetailServiceImpl.loginUser(authLoginRequest), HttpStatus.OK);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponse> signup(@RequestBody @Valid AuthRegisterUserRequest authRegisterUserRequest) {

        return new ResponseEntity<>(this.userDetailServiceImpl.registerUser(authRegisterUserRequest), HttpStatus.OK);
    }
}
