package com.example.ecommerce.service;

import com.example.ecommerce.dataToobject.OrderResponseDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @KafkaListener(topics = "order-response-topic",groupId = "notification-group")
    public void consume(OrderResponseDto orderResponseDto){
        System.out.println("Sending Notification to: " +orderResponseDto.getUserName());

        if (orderResponseDto.getOrderStatus().equals("COMPLETED")){
            System.out.println("Notification Sent to: "+orderResponseDto.getUserName());
        }else {
            System.out.println("Order "+orderResponseDto.getOrderId()+"Failed Successfully");
        }

    }
}
