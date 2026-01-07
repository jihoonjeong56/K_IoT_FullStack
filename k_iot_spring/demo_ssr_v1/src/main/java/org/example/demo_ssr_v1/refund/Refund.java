package org.example.demo_ssr_v1.refund;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.demo_ssr_v1.payment.Payment;
import org.example.demo_ssr_v1.user.User;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@NoArgsConstructor
@Data
@Entity
@Table(name = "refund_tb")
public class Refund {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 결제내역 -> 환불 정책(전체 환불 정책)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false, unique = true)
    private Payment payment;

    // 사용자가 환불 할 사유
    @Column(length = 500)
    private String reason;
    // 관리자 환불 거절 사유

    @Column(length = 500)
    private String rejectReason;

    // 환불 상태 (대기, 승인, 거절)
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RefundStatus status = RefundStatus.PENDING; // 기본값 대기

    // 생성 시간
    @CreationTimestamp
    private Timestamp createdAt;

    // 수정 시간
    @CreationTimestamp
    private Timestamp updatedAt;


    // 사용자가 먼저 환불 요청에 의해서 -> row 생성 되기 때문
    @Builder
    public Refund(User user, Payment payment, String reason){
        this.user = user;
        this.payment = payment;
        this.reason = reason;
        this.status = RefundStatus.PENDING;
    }

    // 편의 기능
    // 환불 승인 처리
    public void approve(){
        this.status = RefundStatus.APPROVED;
    }
    public void reject(String rejectReason){
        this.status = RefundStatus.REJECTED;
        this.rejectReason = rejectReason;
    }

    // 대기중인 상태인지 확인
    public boolean isPending(){
        return this.status == RefundStatus.PENDING;
    }

    // 승인된 상태인지 확인
    public boolean isApproved(){
        return this.status == RefundStatus.APPROVED;
    }

    // 거절된 상태인지
    public boolean isRejected(){
        return this.status == RefundStatus.REJECTED;
    }

}
