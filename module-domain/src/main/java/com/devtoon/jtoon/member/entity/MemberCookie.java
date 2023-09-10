package com.devtoon.jtoon.member.entity;

import static com.devtoon.jtoon.error.model.ErrorStatus.*;
import static java.util.Objects.*;

import com.devtoon.jtoon.error.exception.InvalidRequestException;
import com.devtoon.jtoon.global.common.BaseTimeEntity;

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

@Entity
@Getter
@Table(name = "member_cookies")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberCookie extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_cookie_id")
	private Long id;

	@Column(name = "cookie_count", nullable = false)
	private int cookieCount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Builder
	public MemberCookie(int cookieCount, Member member) {
		if (cookieCount < 0) {
			throw new InvalidRequestException(COOKIE_COUNT_NOT_NEGATIVE);
		}

		this.cookieCount = cookieCount;
		this.member = requireNonNull(member, MEMBER_IS_NULL.getMessage());
	}
}
