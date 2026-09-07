package com.example.shop.payment;

import com.example.shop.payment.exception.PaymentDeclinedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.NoSuchElementException;

// SMELL: the "integration" with the payment gateway is really just a
// business rule (the credit limit) hardcoded into the service - no
// port/adapter, so you can't swap it out or easily test it without going
// through PaymentService.
@Service
public class PaymentService {

    private static final BigDecimal CREDIT_LIMIT = new BigDecimal("10000");

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public Payment charge(Long orderId, BigDecimal amount) {
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setAmount(amount);
        payment.setCreatedAt(Instant.now());

        if (amount.compareTo(CREDIT_LIMIT) > 0) {
            payment.setStatus(PaymentStatus.DECLINED);
            paymentRepository.save(payment);
            throw new PaymentDeclinedException(orderId, amount);
        }

        payment.setStatus(PaymentStatus.CHARGED);
        return paymentRepository.save(payment);
    }

    @Transactional
    public void refund(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NoSuchElementException("Unknown payment: " + paymentId));
        payment.setStatus(PaymentStatus.REFUNDED);
        paymentRepository.save(payment);
    }

    public Payment findByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NoSuchElementException("No payment for order: " + orderId));
    }
}
