package org.example.demo_ssr_v1.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // 기본적인 CRUD 만들어진 상태 ..

    // imp_uid로 결제 내역 조회
    // 포트원 결제 번호로  Payment 정보 조회 쿼리 자동 생성됩;
    Optional<Payment> findByImpUid(String impUid);

    Optional<Payment> findByMerchantUid(String merchantUid);

    @Query("SELECT COUNT(p) > 0 FROM Payment p WHERE p.merchantUid = :merchantUid")
    boolean existsByMerchantUid(@Param("merchantUid") String merchantUid);

    List<Payment> findAllByUserId(Long userId);
}
