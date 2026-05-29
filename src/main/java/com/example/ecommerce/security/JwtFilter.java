package com.example.ecommerce.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    public JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if(path.startsWith("/auth")){
            filterChain.doFilter(request,response);
            return;
        }
        String authHeader = request.getHeader("Authorization");
        if(authHeader==null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;

        }
        String token = authHeader.substring(7);
        try {
            String userName = jwtUtil.getUserName(token);

            if (userName!=null){
                System.out.println("User Exist");
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        userName,
                        null,
                        Collections.singleton(new SimpleGrantedAuthority("USER"))
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        }catch (Exception ex){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        filterChain.doFilter(request,response);
    }
}
