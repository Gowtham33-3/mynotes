package com.myorg.mynotes.service;

import com.myorg.mynotes.dto.SignInRequestDto;
import com.myorg.mynotes.dto.SignInResponseDto;
import com.myorg.mynotes.entity.Role;
import com.myorg.mynotes.entity.User;
import com.myorg.mynotes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public SignInResponseDto createUser(SignInRequestDto request) throws RuntimeException{

        if(userRepository.existsByUserName(request.getEmail())){
            throw new RuntimeException("email is already registered");
        }

        if(userRepository.existsByUserName(request.getUserName())){
            throw new RuntimeException("UserName is already available");
        }
        SignInResponseDto response =  new SignInResponseDto();
        try {
            User user = new User();
            user.setUserName(request.getUserName());
            user.setEmail(request.getEmail());
            user.setRole(request.getRole() != null ? Role.valueOf(request.getRole()) : Role.USER);
            String hashedPassword =
                    passwordEncoder.encode(request.getPassword());
            user.setPasswordHash(hashedPassword);
            user.setCreatedAt(LocalDateTime.now());
            userRepository.save(user);
        }catch(Exception ex){
            System.out.println("error occoured while creating user");
            response.setResponseCode("500");
            response.setMessage("error occurred while registration");
            return response;
        }
        response.setResponseCode("200");
        response.setMessage("sign in successful");
        return response;
    }
}
