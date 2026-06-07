package com.example.ecommerce.service;

import com.example.ecommerce.dataToobject.OrderDto;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repo;

    @Autowired
    private KafkaProduceService kafkaProduceService;

    public Order placeOrder(OrderDto orderDto){
        Order order = new Order();
        order.setProductId(orderDto.getProductId());
        order.setQuantity(orderDto.getQuantity());
        order.setUserId(orderDto.getUserId());
        order.setUserName(orderDto.getUserName());
        order.setStatus("CREATED");
        Order saveOrder = repo.save(order);
        orderDto.setOrderId(saveOrder.getId());
        kafkaProduceService.sendMessage(orderDto);
        return saveOrder;
    }
}
