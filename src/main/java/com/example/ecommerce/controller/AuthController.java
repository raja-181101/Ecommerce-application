package com.example.ecommerce.controller;

import com.example.ecommerce.dataToobject.AuthRequest;
import com.example.ecommerce.dataToobject.AuthResponse;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public String register(@RequestBody User user){
        if(repo.findByuserName(user.getUserName())!=null){
            return "User Already Exist!";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        repo.save(user);
        return "User Registered Successful";
    }

    @PostMapping("/login")
        public AuthResponse login(@RequestBody AuthRequest authRequest){
        User user = repo.findByuserName(authRequest.userName);
        if (user!=null && passwordEncoder.matches(authRequest.password,user.getPassword())) {
            String accessToken = jwtUtil.generateAccessToken(user.getUserName(), user.getRole());
            String refreshToken = jwtUtil.generateRefreshToken(user.getUserName());

            AuthResponse authResponse = new AuthResponse();
            authResponse.setAccessToken(accessToken);
            authResponse.setRefreshToken(refreshToken);
            return authResponse;
        }

        throw new RuntimeException("Invalid username or password");
    }

    @PostMapping("/refresh")
    public String refresh(@RequestBody Map<String,String> request){
        String refreshToken = request.get("refreshToken");
        String userName = jwtUtil.getUserName(refreshToken);
        return jwtUtil.generateAccessToken(userName,"USER");
    }

}
