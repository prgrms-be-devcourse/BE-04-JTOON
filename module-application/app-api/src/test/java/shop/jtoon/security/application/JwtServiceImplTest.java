package shop.jtoon.security.application;

import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import shop.jtoon.exception.UnauthorizedException;
import shop.jtoon.security.service.RefreshTokenService;

import java.nio.charset.StandardCharsets;
import java.security.Key;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class JwtServiceImplTest {

    @InjectMocks
    JwtServiceImpl jwtServiceImpl;

    @Mock
    RefreshTokenService refreshTokenService;

    Key secretKey;

    @BeforeEach
    void initProperties() {
        String test = "testtesttesttesttesttesttesttesttesttesttesttesttesttest";
        ReflectionTestUtils.setField(jwtServiceImpl, "ISS", "test");
        ReflectionTestUtils.setField(jwtServiceImpl, "SALT", test);
        ReflectionTestUtils.setField(jwtServiceImpl, "ACCESS_EXPIRE", 600);
        ReflectionTestUtils.setField(jwtServiceImpl, "REFRESH_EXPIRE", 10080);
        secretKey = Keys.hmacShaKeyFor(test.getBytes(StandardCharsets.UTF_8));
        ReflectionTestUtils.setField(jwtServiceImpl, "secretKey", secretKey);
    }

    @DisplayName("Access Token 생성 성공 테스트")
    @Test
    void accessToken_create_success() {
        // given
        String email = "abc@gmail.com";

        // when, then
        assertThatNoException().isThrownBy(() -> jwtServiceImpl.generateAccessToken(email));
    }

    @DisplayName("Refresh Token 생성 성공 테스트")
    @Test
    void refreshToken_create_success() {
        // when, then
        assertThatNoException().isThrownBy(() -> jwtServiceImpl.generateRefreshToken());
    }

    @DisplayName("Refresh Token이 존재하지 않으면 예외 발생 테스트")
    @Test
    void refreshToken_notExists_thenThrowException() {
        // given
        String refreshToken = jwtServiceImpl.generateRefreshToken();
        given(refreshTokenService.hasRefreshToken(refreshToken)).willReturn(Boolean.FALSE);

        // when, then
        assertThatThrownBy(() -> jwtServiceImpl.validateRefreshTokenRedis(refreshToken))
                .isInstanceOf(UnauthorizedException.class);
    }

    @DisplayName("Refresh Token 업데이트 성공 테스트")
    @Disabled
    @Test
    void updateRefreshToken_success() {
        // given
        String accessToken = jwtServiceImpl.generateAccessToken("abc@gmail.com");
        String oldRefreshToken = jwtServiceImpl.generateRefreshToken();
        String newRefreshToken = jwtServiceImpl.generateRefreshToken();

        // when, then
        assertThatNoException().isThrownBy(() -> jwtServiceImpl.updateRefreshTokenDb(accessToken, newRefreshToken, oldRefreshToken));
    }
}
