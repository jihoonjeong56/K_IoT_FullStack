package org.example.demo_ssr_v1.payment;

import lombok.Data;
import org.example.demo_ssr_v1._core.errors.exception.Exception400;
import org.springframework.validation.annotation.Validated;


public class PaymentRequest {

    @Data
    // 결제 요청 생성 DTO
    public static class PrepareDTO{
        private Integer amount; // 충전할 금액

        public void validate(){
            if(amount == null || amount <=0 ){
                throw new Exception400("충전할 포인트는 0보다 커야 합니다.");
            }
            if(amount < 100){
                throw new Exception400("최소 충전 금액은 100 포인트 입니다.");
            }
            if(amount > 100000){
                throw new Exception400("최대 충전할 금액은 100,000 포인트 입니다.");
            }
        }

    }

    // 결제 검증 요청 DTO
    @Data
    public static class VerifyDTO{
        private String impUid; // 포트원 결제 고유번호
        private String merchantUid; // 우리서버 주문 번호

        public void validate(){
            if(impUid==null || impUid.trim().isEmpty()){
                throw new Exception400("결제 고유번호가 필요합니다.");
            }
            if(merchantUid==null || merchantUid.trim().isEmpty()){
                throw new Exception400("결제 주분번호가 필요합니다.");
            }
        }
    }
}
