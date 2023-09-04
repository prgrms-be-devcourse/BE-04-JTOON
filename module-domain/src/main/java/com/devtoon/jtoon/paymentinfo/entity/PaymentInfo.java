package com.devtoon.jtoon.paymentinfo.entity;

import static java.util.Objects.*;

import com.devtoon.jtoon.member.entity.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "payments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payment_id")
	private Long id;

	@Column(name = "imp_uid", length = 100, nullable = false, unique = true, updatable = false)
	private String impUid;    // 포트원 결제 고유번호

	@Column(name = "merchant_uid", length = 100, nullable = false, unique = true, updatable = false)
	private String merchantUid;    // 가맹점 주문번호

	@Column(name = "pg", length = 20, nullable = false)
	private PG pg;    // 결제사

	@Column(name = "pay_method", length = 20, nullable = false)
	private String payMethod;    // 결제 방법

	@Column(name = "product_name", length = 15, nullable = false)
	private String productName;        // 상품명

	@Column(name = "amount", nullable = false)
	private int amount;        // 결제 금액

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Builder
	private PaymentInfo(
		String impUid,
		String merchantUid,
		PG pg,
		String payMethod,
		String productName,
		int amount,
		Member member
	) {
		if (amount <= 0) {
			throw new RuntimeException("amount is zero or negative number");
		}

		this.impUid = requireNonNull(impUid, "impUid is null");
		this.merchantUid = requireNonNull(merchantUid, "merchantUid is null");
		this.pg = requireNonNull(pg, "pg is null");
		this.payMethod = requireNonNull(payMethod, "payMethod is null");
		this.productName = requireNonNull(productName, "productName is null");
		this.amount = amount;
		this.member = requireNonNull(member, "member is null");
	}
}
