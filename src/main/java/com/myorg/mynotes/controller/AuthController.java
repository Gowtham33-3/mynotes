package com.myorg.mynotes.controller;

import com.myorg.mynotes.dto.SignInRequestDto;
import com.myorg.mynotes.dto.SignInResponseDto;
import com.myorg.mynotes.service.AuthService;
import com.myorg.mynotes.service.JwtService;
import com.myorg.mynotes.dto.LoginRequest;
import com.myorg.mynotes.dto.LoginResponse;
import com.myorg.mynotes.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthService authService;

    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponseDto> signIn(@RequestBody SignInRequestDto request){
       return  ResponseEntity.ok(userService.createUser(request));
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request, HttpServletResponse response){
        return authService.login(request, response);
    }


    @PostMapping("/logout")
    public LoginResponse logout(@RequestBody LoginRequest request){

        return new LoginResponse();
    }




}
