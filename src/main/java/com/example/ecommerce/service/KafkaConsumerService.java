package com.example.ecommerce.service;

import com.example.ecommerce.dataToobject.OrderDto;
import com.example.ecommerce.dataToobject.OrderResponseDto;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;

    @Autowired
    private ProductRepository repo;

    @KafkaListener(topics = "order-topic",containerFactory = "orderKafkaListenerFactory",groupId = "my-group")
    public void consume(OrderDto event){

        Product product = repo.findById(event.getProductId()).orElseThrow(()->new RuntimeException("Product Not Found"));
        OrderResponseDto responseDto = new OrderResponseDto();
        responseDto.setOrderId(event.getOrderId());

        if(product.getQuantity()> event.getQuantity()){
            product.setQuantity(product.getQuantity()-event.getQuantity());
            repo.save(product);
            responseDto.setOrderStatus("COMPLETED");
            responseDto.setUserId(event.getUserId());
            responseDto.setUserName(event.getUserName());
            System.out.println("Stock Updated");
        }else {
            responseDto.setOrderStatus("FAILED");
        }
        kafkaTemplate.send("order-response-topic",responseDto);
    }
}
