package com.example.ecommerce.controller;

import com.example.ecommerce.dataToobject.OrderDto;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.security.JwtUtil;
import com.example.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    public OrderService orderService;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public JwtUtil jwtUtil;

    @PostMapping
    public Order placeOrder(@RequestBody OrderDto orderDto, @RequestHeader("Authorization") String token){
        String userName = jwtUtil.getUserName(token.substring(7));

        User user = userRepository.findByuserName(userName);
        orderDto.setUserId(user.getId());
        orderDto.setUserName(user.getUserName());
        return orderService.placeOrder(orderDto);
    }
}
