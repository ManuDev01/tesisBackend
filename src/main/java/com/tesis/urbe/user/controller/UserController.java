package com.tesis.urbe.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tesis.urbe.user.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    @GetMapping("/getUsers")
    public ResponseEntity<?> getUsers() {
        return this.
    };
}
