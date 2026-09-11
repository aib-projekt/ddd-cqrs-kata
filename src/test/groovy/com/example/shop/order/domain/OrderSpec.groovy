package com.example.shop.order.domain

import com.example.shop.order.OrderStatus
import spock.lang.Specification

class OrderSpec extends Specification {

    def 'Should reject cancellation of CONFIRMED or FAILED order'() {
        given:
        def order = new Order(id: 123L, status: status)

        when:
        def result = order.cancel()

        then:
        thrown(IllegalStateException)
        result == null

        where:
        status << [OrderStatus.CONFIRMED, OrderStatus.FAILED]
    }

    def 'Should change the status to CANCELLED'() {
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
