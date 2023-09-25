package shop.jtoon.config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import shop.jtoon.dto.MemberDto;
import shop.jtoon.factory.MemberFactory;

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
            MemberDto memberDto = MemberFactory.createMemberDto();
            List<SimpleGrantedAuthority> roles = List.of(new SimpleGrantedAuthority(memberDto.role().toString()));
            Authentication auth = new UsernamePasswordAuthenticationToken(memberDto, BLANK, roles);
            SecurityContextHolder.getContext().setAuthentication(auth);

            return SecurityContextHolder.getContext();
        }
    }
}
