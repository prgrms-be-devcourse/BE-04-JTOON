package shop.jtoon.entity;

import static java.util.Objects.*;

import java.math.BigDecimal;

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
import shop.jtoon.type.ErrorStatus;

@Getter
@Entity
@Table(name = "payments_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentInfo extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payment_info_id")
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

	// TODO : 추후 nullable = false
	@ManyToOne(fetch = FetchType.LAZY)
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
		this.impUid = requireNonNull(impUid, ErrorStatus.PAYMENT_IMP_UID_IS_NULL.getMessage());
		this.merchantUid = requireNonNull(merchantUid, ErrorStatus.PAYMENT_MERCHANT_UID_IS_NULL.getMessage());
		this.payMethod = requireNonNull(payMethod, ErrorStatus.PAYMENT_PAY_METHOD_IS_NULL.getMessage());
		this.cookieItem = requireNonNull(cookieItem, ErrorStatus.PAYMENT_COOKIE_ITEM_IS_NULL.getMessage());
		this.amount = requireNonNull(amount, ErrorStatus.PAYMENT_AMOUNT_IS_NULL.getMessage());

		// TODO : this.member = requireNonNull(member, ExceptionStatus.PAYMENT_MEMBER_IS_NULL.getMessage());
		this.member = member;
	}
}
