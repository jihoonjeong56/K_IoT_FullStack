package org.example.boardback.service.payment.gateway;

import org.example.boardback.dto.payment.request.PaymentApproveRequestDto;

public interface PaymentGateway {

    /**
     * 모든 PG 결제의 공통 프로세스
     *  : 최종 승인 (approve/confirm)
     *
     *      Toss: confirm
     *      Kakao: approve
     *      Mock: 내부 승인
     */
    PaymentResult approve(PaymentApproveRequestDto request, String userId);
}
