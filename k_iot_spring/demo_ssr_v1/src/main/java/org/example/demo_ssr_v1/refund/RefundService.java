package org.example.demo_ssr_v1.refund;

import lombok.RequiredArgsConstructor;
import org.example.demo_ssr_v1._core.errors.exception.Exception400;
import org.example.demo_ssr_v1._core.errors.exception.Exception403;
import org.example.demo_ssr_v1._core.errors.exception.Exception404;
import org.example.demo_ssr_v1.payment.Payment;
import org.example.demo_ssr_v1.payment.PaymentRepository;
import org.example.demo_ssr_v1.user.User;
import org.example.demo_ssr_v1.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefundService {
    private final RefundRepository refundRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    public Payment 환불요청폼화면검증(Long paymentId, Long userId) {
        Payment payment =paymentRepository.findByIdWithUser(paymentId);


        if(!payment.getUser().getId().equals(userId)){
            throw new Exception403("권한 없음");
        }
        if("paid".equals(payment.getStatus())){
            throw new Exception400("결제 완료된 상태만 환불 요청할 수 있습니다.");
        }
        if(refundRepository.findByPaymentId(paymentId).isPresent()){
            throw new Exception400("이미 요청된 값");
        }
        return payment;
    }

    // 1단계 : 사용자가 환불 요청함
    @Transactional
    public void 환불요청(Long userId, RefundRequest.RequestDTO req) {

        // 화면검증 로직 재사용
        Payment payment = 환불요청폼화면검증(req.getPaymentId(), userId);

        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception404("사용자를 찾을 수 없음"));

        Refund refund = Refund.builder()
                .user(user)
                .payment(payment)
                .reason(req.getReason())
                .build();
        // 환불 요청 테이블에 이력 저장 해야함
        refundRepository.save(refund);
    }

    // 결제 내역 목록 조회
}
