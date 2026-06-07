package com.example.ecommerce.service;

import com.example.ecommerce.dataToobject.OrderResponseDto;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderResponseService {

    @Autowired
    private OrderRepository repo;

    @KafkaListener(topics = "order-response-topic",containerFactory = "responseKafkaListenerFactory",groupId = "order-group")
    public void consume(OrderResponseDto orderResponseDto){
        Order order = repo.findById(orderResponseDto.getOrderId()).orElseThrow(()-> new ResourceNotFoundException("Product Not Found"));

        order.setStatus(orderResponseDto.getOrderStatus());
        repo.save(order);
        System.out.println("Order Status Updated: "+orderResponseDto.getOrderStatus());
    }
}
