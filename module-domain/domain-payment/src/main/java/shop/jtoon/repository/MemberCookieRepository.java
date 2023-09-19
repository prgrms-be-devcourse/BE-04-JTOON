package shop.jtoon.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import shop.jtoon.entity.Member;
import shop.jtoon.entity.MemberCookie;

@Repository
public interface MemberCookieRepository extends JpaRepository<MemberCookie, Long> {

	Optional<MemberCookie> findByMember(Member member);

	@Query("SELECT mc FROM MemberCookie mc WHERE mc.member.id = :memberId")
	Optional<MemberCookie> findByMemberId(@Param("memberId") Long memberId);
}
