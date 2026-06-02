package com.example.ecommerce.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf(csrf->csrf.disable())
                .authorizeHttpRequests(auth-> auth.requestMatchers("/auth/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE,"/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/products").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/products").hasAnyRole("USER","ADMIN")
                        .anyRequest()
                        .authenticated()).addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
