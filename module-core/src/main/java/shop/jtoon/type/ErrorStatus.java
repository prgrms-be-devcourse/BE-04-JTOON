package shop.jtoon.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorStatus {

	COMMON_DTO_FIELD_INVALID_FORMAT("올바른 요청 정보가 아닙니다."),

	MEMBER_IS_NULL("회원 값이 NULL 입니다."),
	MEMBER_EMAIL_INVALID_FORMAT("올바른 이메일 형식이 아닙니다."),
	MEMBER_PASSWORD_INVALID_FORMAT("올바른 비밀번호형식이 아닙니다."),
	MEMBER_NAME_INVALID_FORMAT("올바른 이름이 아닙니다."),
	MEMBER_NICKNAME_INVALID_FORMAT("올바른 닉네임이 아닙니다."),
	MEMBER_GENDER_INVALID_FORMAT("올바른 성이 아닙니다."),
	MEMBER_PHONE_INVALID_FORMAT("올바른 전화번호 형식이 아닙니다."),
	MEMBER_ROLE_INVALID_FORMAT("올바른 회원 역할이 아닙니다"),
	MEMBER_LOGIN_TYPE_INVALID_FORMAT("올바른 로그인 타입이 아닙니다."),
	MEMBER_MESSAGE_SEND_FAILED("이메일 인증 메세지 전송 실패"),
	MEMBER_EMAIL_CONFLICT("Email 중복"),
	MEMBER_COOKIE_NOT_FOUND("회원의 쿠키 정보가 존재하지 않습니다."),

	PAYMENT_IMP_UID_IS_NULL("결제 고유번호 값이 NULL 입니다."),
	PAYMENT_MERCHANT_UID_IS_NULL("주문번호 값이 NULL 입니다."),
	PAYMENT_PAY_METHOD_IS_NULL("결제 방법 값이 NULL 입니다."),
	PAYMENT_COOKIE_ITEM_IS_NULL("쿠키 아이템 값이 NULL 입니다."),
	PAYMENT_AMOUNT_IS_NULL("결제 금액 값이 NULL 입니다."),
	PAYMENT_MEMBER_IS_NULL("결제 사용자 값이 NULL 입니다."),
	PAYMENT_IMP_UID_DUPLICATED("결제 고유번호가 중복되었습니다."),
	PAYMENT_MERCHANT_UID_DUPLICATED("주문 번호가 중복되었습니다."),
	PAYMENT_AMOUNT_INVALID("잘못된 결제 금액입니다."),
	PAYMENT_COOKIE_NOT_FOUND("등록되지 않은 쿠키 상품입니다."),

	COOKIE_COUNT_NOT_NEGATIVE("쿠키 개수는 음수일 수 없습니다."),
	COOKIE_MEMBER_NOT_FOUND("해당 회원은 쿠키 정보가 없습니다."),

	WEBTOON_TITLE_DUPLICATED("이미 존재하는 웹툰 제목입니다."),
	WEBTOON_NOT_FOUND("존재하지 않는 웹툰입니다."),
	WEBTOON_NOT_AUTHOR("해당 웹툰의 작가가 아닙니다."),
	WEBTOON_IS_NULL("웹툰 값이 NULL 입니다."),
	WEBTOON_TITLE_IS_NULL("웹툰 제목 값이 NULL 입니다."),
	WEBTOON_DESCRIPTION_IS_NULL("웹툰 설명 값이 NULL 입니다."),
	WEBTOON_AGE_LIMIT_IS_NULL("웹툰 연령 값이 NULL 입니다."),
	WEBTOON_AUTHOR_IS_NULL("웹툰 작가 값이 NULL 입니다."),
	WEBTOON_DAY_OF_WEEK_IS_NULL("웹툰 요일 값이 NULL 입니다."),
	WEBTOON_GENRE_IS_NULL("웹툰 장르 값이 NULL 입니다."),

	EPISODE_IS_NULL("회차 값이 NULL 입니다."),
	EPISODE_NOT_FOUND("존재하지 않는 회차입니다."),
	EPISODE_NUMBER_POSITIVE("회차 번호는 양수여야 합니다."),
	EPISODE_NOT_ENOUGH_COOKIES("쿠키 개수가 부족합니다."),
	EPISODE_TITLE_IS_NULL("회차 제목 값이 NULL 입니다."),
	EPISODE_MAIN_URL_IS_NULL("회차 메인 이미지 URL 값이 NULL 입니다."),
	EPISODE_OPENED_AT_IS_NULL("회차 공개일자 값이 NULL 입니다."),

	S3_UPLOAD_FAIL("S3 이미지 업로드에 실패했습니다."),
	;

	private final String message;
}
