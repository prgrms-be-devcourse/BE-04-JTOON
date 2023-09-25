package shop.jtoon.config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import shop.jtoon.dto.MemberDto;
import shop.jtoon.entity.Gender;
import shop.jtoon.entity.Role;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import static shop.jtoon.util.SecurityConstant.BLANK;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithCurrentUser.WithCustomSecurityContextFactory.class)
public @interface WithCurrentUser {

    final class WithCustomSecurityContextFactory implements WithSecurityContextFactory<WithCurrentUser> {
        @Override
        public SecurityContext createSecurityContext(WithCurrentUser customUser) {
            SecurityContext context = SecurityContextHolder.createEmptyContext();

            MemberDto memberDto = MemberDto.builder()
                    .id(1L)
                    .email("abc@naver.com")
                    .name("test")
                    .nickname("testName")
                    .gender(Gender.MALE)
                    .role(Role.USER)
                    .phone("01054580273")
                    .build();
            context.setAuthentication( new UsernamePasswordAuthenticationToken(memberDto, BLANK,
                    List.of(new SimpleGrantedAuthority(memberDto.role().toString()))));

            return context;
        }
    }

}
