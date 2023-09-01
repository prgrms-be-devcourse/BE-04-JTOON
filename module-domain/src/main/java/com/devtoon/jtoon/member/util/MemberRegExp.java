package com.devtoon.jtoon.member.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * <p>Email: RFC 2822 Format</p>
 * <p>Phone: 01X xxxx xxxx (빈칸 없이) 11자리 핸드폰 번호 입력 가능.</p>
 * <p>Password: 8자 이상, 적어도 하나 이상의 대,소 문자와 숫자를 포함해야하며 특수문자도 가능.</p>
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberRegExp {

	public static final String EMAIL_PATTERN = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
	public static final String PHONE_PATTERN = "^01([0|1|6|7|8|9])+?([0-9]{4})+?([0-9]{4})$";
	public static final String PASSWORD_PATTERN = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$";
}
