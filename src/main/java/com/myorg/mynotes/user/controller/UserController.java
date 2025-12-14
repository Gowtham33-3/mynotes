package com.myorg.mynotes.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/secure-test")
    public String secure() {
        return "Authenticated";
    }
}
