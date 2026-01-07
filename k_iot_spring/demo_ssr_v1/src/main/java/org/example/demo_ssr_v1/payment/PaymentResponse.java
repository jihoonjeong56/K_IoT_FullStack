package org.example.demo_ssr_v1.payment;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.example.demo_ssr_v1._core.utils.MyDateUtil;

public class PaymentResponse {
    @Data
    public static class PrepareDTO {
        private String merchantUid;
        private Integer amount;
        private String impKey; // 포트원 RestAPI 키


        public PrepareDTO(String merchantUid, Integer amount, String impKey) {
            this.merchantUid = merchantUid;
            this.amount = amount;
            this.impKey = impKey;
        }
    }

    // 결제 응답 DTO - JS 로 내려줄 데이터
    @Data
    public static class VerifyDTO {
        private Integer amount;
        private Integer currentPoint;

        public VerifyDTO(Integer amount, Integer currentPoint) {
            this.amount = amount;
            this.currentPoint = currentPoint;
        }
    }

    //포트원 엑세스 토큰 응답 DTO 설계
    @Data
    public static class PortOneTokenResponse {
        private int code;
        private String message;
        // 중첩 객체를 설계해야함
        private ResponseDate response;

        @Data
        @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class ResponseDate {
            private String accessToken;
            private int now;
            private int expiredAt;
        }
    }

    // 포트원 결제 조회 응답 DTO
    @Data
    public static class PortOnePaymentResponse {
        private int code;
        private String message;
        private PaymentData response;

        @Data
        @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class PaymentData {
            private int amount;
            private String impUid;
            private String merchantUid;
            private String status;
            private Long paidAt;

        }
    }

    @Data
    public static class ListDTO {
        private Long id;
        private String merchantUid;
        private String impUid;
        private String paidAt;
        private String status;
        private Integer amount;
        private Boolean isRefundable; // 환불 가능 여부(화면에 표시여부)


        public ListDTO(Payment payment, Boolean isRefundable) {
            this.id = payment.getId();
            this.impUid = payment.getImpUid();
            this.merchantUid = payment.getMerchantUid();
            this.amount = payment.getAmount();
            this.status = payment.getStatus();
            this.isRefundable = isRefundable != null ? isRefundable : false;

            // 상태 표시명 변환
            if ("paid".equals(payment.getStatus())) {
                this.status = "결제완료";
            } else {
                this.status = "환불완료";
            }

            // 날자 포멧팅
            if (payment.getCreatedAt() != null) {
                this.paidAt = MyDateUtil.timestampFormat(payment.getCreatedAt());
            }
        }
    }
}
