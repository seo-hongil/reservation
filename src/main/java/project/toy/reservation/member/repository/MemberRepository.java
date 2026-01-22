package project.toy.reservation.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.toy.reservation.member.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByName(String username);
    Optional<Member> findByEmail(String email);
    boolean existsByEmail(String email);
}
