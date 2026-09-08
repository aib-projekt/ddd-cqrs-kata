package com.example.shop.order;

import com.example.shop.inventory.InventoryService;
import com.example.shop.inventory.exception.InsufficientStockException;
import com.example.shop.order.dto.CreateOrderRequest;
import com.example.shop.order.dto.OrderLineRequest;
import com.example.shop.payment.Payment;
import com.example.shop.payment.PaymentService;
import com.example.shop.payment.exception.PaymentDeclinedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// This test is deliberately a "characterization test" - it documents
// TODAY'S behaviour of OrderService (smells included), so you can refactor
// safely and be confident the outward behaviour hasn't changed (unless you
// deliberately change it, e.g. by fixing cancelOrder).
class OrderServiceTest {

    private final OrderRepository orderRepository = mock(OrderRepository.class);
    private final InventoryService inventoryService = mock(InventoryService.class);
    private final PaymentService paymentService = mock(PaymentService.class);
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(orderRepository, inventoryService, paymentService);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            if (order.getId() == null) {
                order.setId(1L);
            }
            return order;
        });
    }

    @Test
    void happyPath_reservesStockAndChargesPaymentThenConfirms() {
        CreateOrderRequest request = new CreateOrderRequest(
                "customer-1",
                List.of(new OrderLineRequest("sku-1", 2, new BigDecimal("50.00")))
        );
        when(paymentService.charge(any(), any())).thenReturn(new Payment());

        Order result = orderService.createOrder(request);

        assertThat(result.getStatus()).isEqualTo(OrderStatus.CONFIRMED);
        verify(inventoryService).reserveStock("sku-1", 2);
        verify(inventoryService).confirmStock("sku-1", 2);
        verify(paymentService).charge(any(), any());
    }

    @Test
    void insufficientStock_releasesAlreadyReservedLinesAndMarksOrderFailed() {
        CreateOrderRequest request = new CreateOrderRequest(
                "customer-1",
                List.of(
                        new OrderLineRequest("sku-1", 1, new BigDecimal("10.00")),
                        new OrderLineRequest("sku-2", 5, new BigDecimal("10.00"))
                )
        );
        // the first line reserves fine, the second one throws
        doNothingThenThrow();

        assertThatThrownBy(() -> orderService.createOrder(request))
                .isInstanceOf(InsufficientStockException.class);

        verify(inventoryService).releaseStock("sku-1", 1);
        verify(paymentService, times(0)).charge(any(), any());

        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository, times(2)).save(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo(OrderStatus.FAILED);
    }

    @Test
    void paymentDeclined_releasesAllReservedStockAndMarksOrderFailed() {
        CreateOrderRequest request = new CreateOrderRequest(
                "customer-1",
                List.of(new OrderLineRequest("sku-1", 1, new BigDecimal("99999.00")))
        );
        when(paymentService.charge(any(), any()))
                .thenThrow(new PaymentDeclinedException(1L, new BigDecimal("99999.00")));

        assertThatThrownBy(() -> orderService.createOrder(request))
                .isInstanceOf(PaymentDeclinedException.class);

        verify(inventoryService).releaseStock("sku-1", 1);
    }

    @Test
    void cancelOrder_marksCancelled_butDoesNotTouchStockOrPayment() {
        Order existing = new Order();
        existing.setId(5L);
        existing.setStatus(OrderStatus.STOCK_RESERVED);
        when(orderRepository.findById(5L)).thenReturn(Optional.of(existing));

        Order result = orderService.cancelOrder(5L);

        assertThat(result.getStatus()).isEqualTo(OrderStatus.CANCELLED);
        // This is NOT "correct" business behaviour - it documents today's
        // gap (see README), which is worth fixing during the refactoring.
        verify(inventoryService, times(0)).releaseStock(anyString(), anyInt());
    }

    private void doNothingThenThrow() {
        org.mockito.Mockito.doNothing()
                .doThrow(new InsufficientStockException("sku-2", 5, 2))
                .when(inventoryService).reserveStock(anyString(), anyInt());
    }
}
