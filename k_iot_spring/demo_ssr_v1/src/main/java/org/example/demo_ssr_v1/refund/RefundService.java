package org.example.demo_ssr_v1.refund;

import lombok.RequiredArgsConstructor;
import org.example.demo_ssr_v1.payment.PaymentRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefundService {
    private final RefundRepository refundRepository;
    private final PaymentRepository paymentRepository;

    // 결제 내역 목록 조회
}
