package org.example.boardback.service.payment.gateway;

import org.example.boardback.dto.payment.request.PaymentApproveRequestDto;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MockPaymentGateway implements PaymentGateway {

    @Override
    public PaymentResult approve(PaymentApproveRequestDto request, String userId) {
        // 항상 성공한다고 가정하는 모의 결제
        String paymentKey = "MOCK-" + UUID.randomUUID();

        return PaymentResult.ok(paymentKey);
    }
}