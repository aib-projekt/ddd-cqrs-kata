package com.example.shop.common.configuration;

import com.example.shop.order.aplication.port.OrderRepository;
import com.example.shop.order.domain.Order;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderConfiguration {

    private OrderRepository orderRepository;

    @Bean
    public Order order() {
        return new Order(orderRepository);
    }
}
