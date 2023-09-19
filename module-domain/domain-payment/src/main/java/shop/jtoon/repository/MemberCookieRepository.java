package shop.jtoon.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import shop.jtoon.entity.Member;
import shop.jtoon.entity.MemberCookie;

@Repository
public interface MemberCookieRepository extends JpaRepository<MemberCookie, Long> {

	Optional<MemberCookie> findByMember(Member member);
}
