package com.example.shop.order.domain

import com.example.shop.order.OrderStatus
import com.example.shop.order.common.exception.OrderCancellationException
import spock.lang.Specification
import spock.lang.Unroll

class OrderSpec extends Specification {

    @Unroll("#status")
    def 'Should reject cancel of order with status'() {
        given:
        def order = new Order(id: 123L, status: status)

        when:
        def result = order.cancel()

        then:
        thrown(OrderCancellationException)
        result == null

        where:
        status << [OrderStatus.CONFIRMED, OrderStatus.FAILED]
    }

    @Unroll("#status")
    def 'Should cancel the order'() {
        given:
        def order = new Order(id: 123L, status: status)

        when:
        def result = order.cancel()

        then:
        result.getStatus() == OrderStatus.CANCELLED

        where:
        status << [OrderStatus.CREATED, OrderStatus.CANCELLED, OrderStatus.STOCK_RESERVED, OrderStatus.PAID]
    }
}
