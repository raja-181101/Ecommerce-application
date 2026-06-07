package com.example.ecommerce.controller;

import com.example.ecommerce.dataToobject.OrderDto;
import com.example.ecommerce.service.KafkaProduceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kafka")
public class KafkaController {

    @Autowired
    public KafkaProduceService kafkaProduceService;

    @PostMapping("/msg")
    public String send(@RequestBody OrderDto event){
        kafkaProduceService.sendMessage(event);
        return "Message Sent";
    }
}
