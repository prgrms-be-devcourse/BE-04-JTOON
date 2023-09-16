package shop.jtoon.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityConstant {

	public static final String ACCESS_TOKEN_HEADER = "Access_Token";
	public static final String REFRESH_TOKEN_HEADER = "Refresh_Token";
	public static final String BEARER_VALUE = "Bearer";
	public static final String BLANK = " ";
	public static final String SPLIT_DATA = "\\+";
	public static final int MINUTE = 1000 * 60;
}
