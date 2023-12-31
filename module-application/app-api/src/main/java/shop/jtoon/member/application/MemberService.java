package shop.jtoon.member.application;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.jtoon.dto.MemberDto;
import shop.jtoon.dto.OAuthSignUpDto;
import shop.jtoon.entity.LoginType;
import shop.jtoon.entity.Member;
import shop.jtoon.exception.DuplicatedException;
import shop.jtoon.exception.InvalidRequestException;
import shop.jtoon.exception.NotFoundException;
import shop.jtoon.member.request.LocalSignUpReq;
import shop.jtoon.repository.MemberRepository;
import shop.jtoon.security.request.LoginReq;
import shop.jtoon.security.service.JwtService;
import shop.jtoon.security.service.RefreshTokenService;
import shop.jtoon.security.util.TokenCookie;

import java.util.Optional;

import static shop.jtoon.type.ErrorStatus.*;
import static shop.jtoon.util.SecurityConstant.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final JwtService jwtServiceImpl;
    private final RefreshTokenService refreshTokenServiceImpl;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(LocalSignUpReq localSignUpReq) {
        validateDuplicateEmail(localSignUpReq.email());
        String encryptedPassword = passwordEncoder.encode(localSignUpReq.password());
        Member member = localSignUpReq.toEntity(encryptedPassword);

        memberRepository.save(member);
    }

    public void validateDuplicateEmail(String email) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new DuplicatedException(MEMBER_EMAIL_CONFLICT);
        }
    }

    @Transactional
    public void loginMember(LoginReq loginReq, HttpServletResponse response) {
        Member member = findByEmail(loginReq.email());

        if (!passwordEncoder.matches(loginReq.password(), member.getPassword())) {
            throw new InvalidRequestException(MEMBER_WRONG_LOGIN_INFO);
        }

        if (!member.getLoginType().equals(LoginType.LOCAL)) {
            throw new InvalidRequestException(MEMBER_DUPLICATE_SOCIAL_LOGIN);
        }

        member.updateLastLogin();

        String accessToken = jwtServiceImpl.generateAccessToken(loginReq.email());
        String refreshToken = jwtServiceImpl.generateRefreshToken();
        refreshTokenServiceImpl.saveRefreshToken(refreshToken, loginReq.email());

        response.addCookie(TokenCookie.of(ACCESS_TOKEN_HEADER, accessToken));
        response.addCookie(TokenCookie.of(REFRESH_TOKEN_HEADER, refreshToken));
    }

    @Transactional
    public Member generateOrGetSocialMember(OAuthSignUpDto OAuthSignUpDto) {
        Optional<Member> member = memberRepository.findByEmail(OAuthSignUpDto.email());

        return member.orElseGet(() -> memberRepository.save(OAuthSignUpDto.toEntity(BLANK)));
    }

    public MemberDto findMemberDtoByEmail(String email) {
        Member member = findByEmail(email);

        return MemberDto.toDto(member);
    }

    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MEMBER_NOT_FOUND));
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(MEMBER_EMAIL_NOT_FOUND));
    }
}
