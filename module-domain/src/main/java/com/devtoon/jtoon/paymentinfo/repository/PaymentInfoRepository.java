package com.devtoon.jtoon.paymentinfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devtoon.jtoon.paymentinfo.entity.PaymentInfo;

@Repository
public interface PaymentInfoRepository extends JpaRepository<PaymentInfo, Long> {

	boolean existsByImpUid(String impUid);

	boolean existsByMerchantUid(String merchantUid);
}
