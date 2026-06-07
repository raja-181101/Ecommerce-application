package com.example.ecommerce.service;

import com.example.ecommerce.dataToobject.OrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProduceService {

    @Autowired
    public KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage(OrderDto event){
        kafkaTemplate.send("order-topic",event);
    }
}
