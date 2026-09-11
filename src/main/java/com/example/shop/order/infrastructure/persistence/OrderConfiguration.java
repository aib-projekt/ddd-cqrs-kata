package com.example.shop.order.infrastructure.persistence;

import com.example.shop.order.application.CancelOrderService;
import com.example.shop.order.application.RetrieveOrderDataService;
import com.example.shop.order.application.port.in.CancelOrder;
import com.example.shop.order.application.port.in.RetrieveOrderData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories
public class OrderConfiguration {

    @Bean
    RetrieveOrderData retrieveOrderDataService(WrappedOrderRepository wrappedOrderRepository) {
        return new RetrieveOrderDataService(new OrderRepositoryAdapter(wrappedOrderRepository));
    }

    @Bean
    CancelOrder cancelOrder(WrappedOrderRepository wrappedOrderRepository) {
        return new CancelOrderService(new OrderRepositoryAdapter(wrappedOrderRepository));
    }
}
