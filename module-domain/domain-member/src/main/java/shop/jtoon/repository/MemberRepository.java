package shop.jtoon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import shop.jtoon.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByEmail(String email);
}
