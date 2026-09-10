package com.example.shop.order.adapter.in.web;

import com.example.shop.order.application.port.in.RetrieveOrderData;
import com.example.shop.order.application.query.OrderDetails;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrderController {

    private final RetrieveOrderData retrieveOrderData;

    @GetMapping("/{id}")
    public OrderDetails getOrder(@PathVariable Long id) {
        return retrieveOrderData.getOrder(id);
    }
}
