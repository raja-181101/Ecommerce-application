package com.example.ecommerce.controller;

import com.example.ecommerce.dataToobject.AuthRequest;
import com.example.ecommerce.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest authRequest){
        if("admin".equals(authRequest.userName) && "password".equals(authRequest.password)){
            return jwtUtil.generateToken(authRequest.userName);
        }
        throw new RuntimeException("Invalid Details");

    }
}
