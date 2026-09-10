package com.example.shop.order.application

import spock.lang.Specification

class CancelOrderServiceSpec extends Specification {

    def 'should successfully cancel order'() {
        given:
        def cancelOrder = new CancelOrderService()

        when:
        def orderCancelled = cancelOrder.cancelOrder(123L)

        then:
        orderCancelled != null

    }
}
