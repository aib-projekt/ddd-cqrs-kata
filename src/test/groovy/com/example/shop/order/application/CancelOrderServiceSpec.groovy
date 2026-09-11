package com.example.shop.order.application

import com.example.shop.order.OrderStatus
import com.example.shop.order.domain.Order
import com.example.shop.order.infrastructure.persistence.OrderRepositoryAdapter
import spock.lang.Specification

class CancelOrderServiceSpec extends Specification {

    def 'should successfully cancel order'() {
        given:
        def order = new Order(id: 123L, status: OrderStatus.CREATED)
        and:
        def orderRepository = Mock(OrderRepositoryAdapter)
        and:
        def service = new CancelOrderService(orderRepository)

        when:
        def orderCancelled = service.cancelOrder(123L)

        then:
        orderCancelled != null
        1 * orderRepository.getById(123L) >> Optional.of(order)
        1 * orderRepository.save(_ as Order) >> order
        orderCancelled.id() == 123L
        order.getStatus() == OrderStatus.CANCELLED
    }
}
