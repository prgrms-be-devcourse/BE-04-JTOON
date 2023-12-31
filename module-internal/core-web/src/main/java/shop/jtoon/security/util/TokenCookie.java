package shop.jtoon.security.util;

import static shop.jtoon.util.SecurityConstant.*;

import jakarta.servlet.http.Cookie;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
