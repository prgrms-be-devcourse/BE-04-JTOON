package shop.jtoon.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import shop.jtoon.entity.Member;
import shop.jtoon.entity.MemberCookie;

public interface MemberCookieRepository extends JpaRepository<MemberCookie, Long> {

	Optional<MemberCookie> findByMember(Member member);
}
