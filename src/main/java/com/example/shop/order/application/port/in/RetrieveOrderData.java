package com.example.shop.order.application.port.in;

import com.example.shop.order.application.query.OrderDetails;

public interface RetrieveOrderData {
    OrderDetails getOrder(Long orderId);
}
