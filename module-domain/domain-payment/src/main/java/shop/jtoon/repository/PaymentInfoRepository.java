package shop.jtoon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import shop.jtoon.entity.PaymentInfo;

@Repository
public interface PaymentInfoRepository extends JpaRepository<PaymentInfo, Long> {

	boolean existsByImpUid(String impUid);

	boolean existsByMerchantUid(String merchantUid);
}
