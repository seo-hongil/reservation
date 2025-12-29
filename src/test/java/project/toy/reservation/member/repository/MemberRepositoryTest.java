package project.toy.reservation.member.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import project.toy.reservation.member.entity.Member;
import project.toy.reservation.member.entity.RoleStatus;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest  // DB 관련 컴포넌트만 띄움
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void testSaveAndFind() {
        // given
        Member member = Member.builder()
                .email("test@example.com")
                .password("password")
                .name("홍길동")
                .role(RoleStatus.USER)
                .build();

        // when
        Member saved = memberRepository.save(member);
        Member find = memberRepository.findById(saved.getId()).orElseThrow();

        // then
        assertThat(find.getEmail()).isEqualTo("test@example.com");
        assertThat(find.getName()).isEqualTo("홍길동");
    }
}
