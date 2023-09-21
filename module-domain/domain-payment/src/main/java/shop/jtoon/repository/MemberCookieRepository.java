package shop.jtoon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.jtoon.entity.Member;
import shop.jtoon.entity.MemberCookie;

import java.util.Optional;

@Repository
public interface MemberCookieRepository extends JpaRepository<MemberCookie, Long> {

    Optional<MemberCookie> findByMember(Member member);
}
