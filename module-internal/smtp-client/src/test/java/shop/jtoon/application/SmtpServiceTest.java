package shop.jtoon.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import shop.jtoon.entity.Mail;

@ExtendWith(MockitoExtension.class)
class SmtpServiceTest {

	@Mock
	JavaMailSenderImpl javaMailSender;

	@Test
	@DisplayName("인증 Mail 생성 성공")
	void create_authentication_mail_success() {
		// given
		String email = "abc@gmail.com";
		String text = "def123";

		// when
		Mail mail = Mail.forAuthentication(email, text);

		// then
		assertAll(
			() -> assertThat(mail.getSubject()).isEqualTo(Mail.DEFAULT_SUBJECT),
			() -> assertThat(mail.getTo()).isEqualTo(email),
			() -> assertThat(mail.getText()).isEqualTo(text)
		);
	}

	@ParameterizedTest
	@CsvSource({
		"abc@gmail.com, ",
		" , abcf",
		" , "
	})
	@DisplayName("인증 메일 생성 실패")
	void create_authentication_mail_fail(String email, String text) {
		// when, then
		assertThatThrownBy(() -> Mail.forAuthentication(email, text))
			.isInstanceOf(NullPointerException.class);
	}
}
