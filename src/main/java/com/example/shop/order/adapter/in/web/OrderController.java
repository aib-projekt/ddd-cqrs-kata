package com.example.shop.order.adapter.in.web;

import com.example.shop.order.application.port.in.CancelOrder;
import com.example.shop.order.application.port.in.RetrieveOrderData;
import com.example.shop.order.application.query.OrderDetails;
import com.example.shop.order.domain.event.OrderCancelled;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrderController {

    private final RetrieveOrderData retrieveOrderData;
    private final CancelOrder cancelOrder;

    @GetMapping("/{id}")
    public OrderDetails getOrder(@PathVariable Long id) {
        return retrieveOrderData.getOrder(id);
    }

    @PostMapping("/{id}/cancel")
    public OrderCancelled cancelOrder(@PathVariable Long id) {
        return cancelOrder.cancelOrder(id);
    }
}
