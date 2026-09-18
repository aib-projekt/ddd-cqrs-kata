package com.example.shop.order.application

import com.example.shop.common.event.publisher.EventPublisher
import com.example.shop.order.OrderStatus
import com.example.shop.order.common.exception.OrderCancellationException
import com.example.shop.order.domain.Order
import com.example.shop.order.domain.event.OrderCancellationApproved
import com.example.shop.order.infrastructure.persistence.OrderRepositoryAdapter
import spock.lang.Specification
import spock.lang.Unroll

class CancelOrderServiceSpec extends Specification {

    def 'should successfully cancel order'() {
        given:
        def order = new Order(id: 123L, status: OrderStatus.CREATED)
        and:
        def eventPublisher = Mock(EventPublisher)
        def orderRepository = Mock(OrderRepositoryAdapter)
        and:
        def service = new CancelOrderService(eventPublisher, orderRepository)
        and:
        def orderCancellationCommand = new OrderCancellationCommand(123L)

        when:
        def orderCancelled = service.cancelOrder(orderCancellationCommand)

        then:
        orderCancelled != null
        1 * orderRepository.getById(123L) >> Optional.of(order)
        1 * orderRepository.save(_ as Order) >> order
        1 * eventPublisher.publish(_ as OrderCancellationApproved) >> _
        orderCancelled.id() == 123L
        order.getStatus() == OrderStatus.CANCELLED
    }

    @Unroll("#status")
    def 'should reject cancellation of order when status is:'() {
        given:
        def order = new Order(id: 123L, status: status)
        and:
        def eventPublisher = Mock(EventPublisher)
        def orderRepository = Mock(OrderRepositoryAdapter)
        and:
        def service = new CancelOrderService(eventPublisher, orderRepository)
        and:
        def orderCancellationCommand = new OrderCancellationCommand(123L)

        when:
        def orderCancelled = service.cancelOrder(orderCancellationCommand)

        then:
        thrown(OrderCancellationException)
        orderCancelled == null
        1 * orderRepository.getById(123L) >> Optional.of(order)
        0 * orderRepository.save(_ as Order) >> order
        0 * eventPublisher.publish(_ as OrderCancellationApproved) >> _

        where:
        status << [OrderStatus.FAILED, OrderStatus.CONFIRMED]
    }
}
