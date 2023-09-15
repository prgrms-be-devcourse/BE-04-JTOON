package shop.jtoon.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import shop.jtoon.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	boolean existsByEmail(String email);

	Optional<Member> findByPhone(String phone);

	Optional<Member> findByEmail(String email);
}
