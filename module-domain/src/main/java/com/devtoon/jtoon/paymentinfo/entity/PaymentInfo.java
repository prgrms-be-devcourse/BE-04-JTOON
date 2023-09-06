package com.devtoon.jtoon.paymentinfo.entity;

import static java.util.Objects.*;

import java.math.BigDecimal;

import com.devtoon.jtoon.member.entity.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

	@Column(name = "pay_method", length = 20, nullable = false)
	private String payMethod;    // 결제 방법

	@Enumerated(EnumType.STRING)
	@Column(name = "cookie_item", nullable = false)
	private CookieItem cookieItem;        // 상품명

	@Column(name = "amount", nullable = false)
	private BigDecimal amount;        // 결제 금액

	@ManyToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "member_id", nullable = false) (임시)
	@JoinColumn(name = "member_id")
	private Member member;

	@Builder
	private PaymentInfo(
		String impUid,
		String merchantUid,
		String payMethod,
		CookieItem cookieItem,
		BigDecimal amount,
		Member member
	) {
		this.impUid = requireNonNull(impUid, "impUid is null");
		this.merchantUid = requireNonNull(merchantUid, "merchantUid is null");
		this.payMethod = requireNonNull(payMethod, "payMethod is null");
		this.cookieItem = requireNonNull(cookieItem, "cookieItem is null");
		this.amount = requireNonNull(amount, "amount is null");
		this.member = member;
		// this.member = requireNonNull(member, "member is null");
	}
}
