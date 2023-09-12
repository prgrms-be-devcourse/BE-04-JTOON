package com.devtoon.jtoon.security.util;

import static com.devtoon.jtoon.security.util.SecurityConstant.*;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.http.Cookie;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenCookie {

	private static final Charset charSet = StandardCharsets.UTF_8;

	public static Cookie of(String name, String value) {
		Cookie cookie = new Cookie(name, URLEncoder.encode(BEARER_VALUE + BLANK + value, charSet));
		cookie.setSecure(true);
		cookie.setHttpOnly(true);
		return cookie;
	}
}
