package shop.jtoon.security.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.HandlerExceptionResolver;
import shop.jtoon.member.application.EmailService;
import shop.jtoon.member.application.MemberService;
import shop.jtoon.member.presentation.MemberController;
import shop.jtoon.payment.application.PaymentService;
import shop.jtoon.payment.presentation.PaymentController;
import shop.jtoon.security.filter.AuthenticationFilter;
import shop.jtoon.security.service.AuthorizationService;
import shop.jtoon.security.service.RefreshTokenService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {MemberController.class, PaymentController.class})
class AuthenticationServiceTest {

    @Autowired
    MemberController memberController;

    @MockBean
    MemberService memberService;

    @MockBean
    EmailService emailService;

    @MockBean
    RefreshTokenService refreshTokenService;

    MockMvc mockMvc;

    @MockBean
    PaymentService paymentService;

    AuthenticationFilter authenticationFilter;

    @Autowired
    HandlerExceptionResolver handlerExceptionResolver;

    @BeforeEach
    void init() {
        authenticationFilter = new AuthenticationFilter(
                handlerExceptionResolver,
                new AuthorizationService(),
                new AuthenticationServiceImpl(memberService),
                new JwtServiceImpl(refreshTokenService)
        );

        mockMvc = MockMvcBuilders.standaloneSetup(memberController)
                .addFilter(authenticationFilter)
                .build();
    }

    @DisplayName("인증없이 접근 성공")
    @Test
    void noAuthentication_access_success() throws Exception {
        // given
        mockMvc.perform(get("/members/email-authorization?email=example@naver.com"))
                .andExpect(status().isCreated());
    }
}
