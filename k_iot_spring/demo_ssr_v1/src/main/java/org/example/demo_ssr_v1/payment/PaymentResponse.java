package org.example.demo_ssr_v1.payment;

import lombok.Data;

public class PaymentResponse {
    @Data
    public static class PrepareDTO{
        private String merchantUid;
        private Integer amount;
        private String impKey; // 포트원 RestAPI 키


        public PrepareDTO(String merchantUid, Integer amount, String impKey) {
            this.merchantUid = merchantUid;
            this.amount = amount;
            this.impKey = impKey;
        }
    }
}
