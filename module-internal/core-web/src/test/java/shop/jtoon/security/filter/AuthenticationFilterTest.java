package shop.jtoon.security.filter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import shop.jtoon.security.service.AuthenticationService;
import shop.jtoon.security.service.AuthorizationService;
import shop.jtoon.security.service.JwtService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.anyString;
import static org.mockito.BDDMockito.given;
import static shop.jtoon.util.SecurityConstant.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationFilterTest {

    @InjectMocks
    AuthenticationFilter authenticationFilter;

    @Mock
    AuthenticationService authenticationService;

    @Mock
    AuthorizationService authorizationService;

    @Mock
    JwtService jwtService;

    MockHttpServletRequest mockHttpServletRequest;
    MockHttpServletResponse mockHttpServletResponse;
    MockFilterChain mockFilterChain;

    public String SALT = "testtesttesttesttesttesttesttesttesttesttest";

    Key secretKey;

    String accessToken;

    String refreshToken;

    @BeforeEach
    void requireMockInit() {
        mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletResponse = new MockHttpServletResponse();
        mockFilterChain = new MockFilterChain();

        secretKey = Keys.hmacShaKeyFor(SALT.getBytes(StandardCharsets.UTF_8));
        Date issueDate = new Date();
        Date exipireDate = new Date(issueDate.getTime() + 123);

        accessToken = Jwts.builder()
                .setIssuer("Access_test")
                .setIssuedAt(issueDate)
                .setExpiration(exipireDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT").compact();

        refreshToken = Jwts.builder()
                .setIssuer("Refresh_test")
                .setIssuedAt(issueDate)
                .setExpiration(exipireDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT").compact();
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("AuthenticationFilter 빈 주입 성공")
    void authenticationFilter_bean_inject_success() {
        // given, when, then
        assertThat(authenticationFilter).isNotNull();
    }

    @Test
    @DisplayName("사용자 인증 실패")
    void authenticate_failed() throws ServletException, IOException {
        // when
        authenticationFilter.doFilterInternal(mockHttpServletRequest, mockHttpServletResponse, mockFilterChain);

        // then
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    @DisplayName("사용자 인증 성공")
    void authenticate_success() throws ServletException, IOException {
        // given
        Cookie[] cookies = {new Cookie(ACCESS_TOKEN_HEADER, BEARER_VALUE + SPLIT_DATA + accessToken)};
        Authentication authentication = new UsernamePasswordAuthenticationToken("test", "test");

        mockHttpServletRequest.setCookies(cookies);
        given(authorizationService.isTokenValid(anyString())).willReturn(true);
        given(authenticationService.getAuthentication(anyString())).willReturn(authentication);

        // when
        authenticationFilter.doFilterInternal(mockHttpServletRequest, mockHttpServletResponse, mockFilterChain);

        // then
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isEqualTo(authentication);
    }

    @Test
    @DisplayName("토큰 재발급 성공")
    void Generate_tokens_success() throws ServletException, IOException {
        // given
        Cookie[] cookies = {
                new Cookie(ACCESS_TOKEN_HEADER, BEARER_VALUE + SPLIT_DATA + accessToken),
                new Cookie(REFRESH_TOKEN_HEADER, BEARER_VALUE + SPLIT_DATA + refreshToken)
        };
        Authentication authentication = new UsernamePasswordAuthenticationToken("test", "test");

        mockHttpServletRequest.setCookies(cookies);
        given(authorizationService.isTokenValid(anyString())).willReturn(false);
        given(jwtService.reGenerateAccessToken(anyString())).willReturn(accessToken);
        given(jwtService.generateRefreshToken()).willReturn(refreshToken);

        given(authenticationService.getAuthentication(anyString())).willReturn(authentication);

        // when
        authenticationFilter.doFilterInternal(mockHttpServletRequest, mockHttpServletResponse, mockFilterChain);

        // then
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isEqualTo(authentication);
        Assertions.assertAll(
                () -> assertThat(mockHttpServletResponse.getCookies()).hasSize(2),
                () -> assertThat(mockHttpServletResponse.getCookies()[0].getValue()).contains(accessToken),
                () -> assertThat(mockHttpServletResponse.getCookies()[1].getValue()).contains(refreshToken)
        );
    }
}
