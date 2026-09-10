package com.example.shop.order.infrastructure.persistence;

import com.example.shop.order.Order;
import com.example.shop.order.OrderLine;
import com.example.shop.order.application.port.out.OrderRepository;
import com.example.shop.order.application.query.OrderDetails;
import com.example.shop.order.application.query.OrderDetailsLine;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class OrderRepositoryAdapter implements OrderRepository {
    private final WrappedOrderRepository wrappedJpaRepo;

    @Override
    public Optional<OrderDetails> findById(Long id) {
        return wrappedJpaRepo.findById(id)
                .map(OrderRepositoryAdapter::mapToOrderDetails);
    }

    @NonNull
    private static OrderDetails mapToOrderDetails(Order order) {
        var lines = order.getLines().stream()
                .map(OrderRepositoryAdapter::mapToOrderDetailsLine)
                .toList();
        return new OrderDetails(order.getId(), order.getStatus().name(), order.getCreatedAt(), order.getTotalAmount(), lines);
    }

    private static OrderDetailsLine mapToOrderDetailsLine(OrderLine line) {
        return new OrderDetailsLine(line.getProductId(), line.getQuantity(), line.getUnitPrice(), line.lineTotal());
    }

}

interface WrappedOrderRepository extends JpaRepository<Order, Long> { }