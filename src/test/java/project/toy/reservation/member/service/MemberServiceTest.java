package project.toy.reservation.member.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.toy.reservation.member.dto.SignupRequest;
import project.toy.reservation.member.entity.Member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional  // 테스트 후 롤백
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Test
    void signUpTest() {
        // given
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("signup@example.com");
        signupRequest.setName("홍길동");
        signupRequest.setPassword("1234");

        // when
        Member saved = memberService.register(signupRequest);

        // then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getEmail()).isEqualTo("signup@example.com");
    }

    @Test
    void duplicateEmailTest() {
        // given
        SignupRequest member1 = new SignupRequest();
        member1.setEmail("dup@example.com");
        member1.setName("홍길동");
        member1.setPassword("1234");

        SignupRequest member2 = new SignupRequest();
        member2.setEmail("dup@example.com");
        member2.setName("홍길동2");
        member2.setPassword("5678");

        // when
        memberService.register(member1);

        // then
        assertThrows(IllegalArgumentException.class, () -> memberService.register(member2));
    }
}
