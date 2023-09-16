package shop.jtoon.security.handler;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import shop.jtoon.type.ErrorStatus;

@Slf4j
@Component
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException exception) throws IOException, ServletException {
		String errorMessage = "";

		log.error("===== OAuthenticationFailure!!! =====", exception);

		if (exception instanceof BadCredentialsException) {
			errorMessage = ErrorStatus.MEMBER_WRONG_LOGIN_FORMAT.toString();
		} else if (exception instanceof InternalAuthenticationServiceException) {
			errorMessage = ErrorStatus.MEMBER_LOGIN_SERVER_ERROR.toString();
		} else if (exception instanceof AuthenticationCredentialsNotFoundException) {
			errorMessage = ErrorStatus.MEMBER_LOGIN_REJECTED.toString();
		}

		setDefaultFailureUrl("/auth/login?error=true&exception=" + errorMessage);

		super.onAuthenticationFailure(request, response, exception);
	}
}
