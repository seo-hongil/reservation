package project.toy.reservation.member.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import project.toy.reservation.member.dto.SignupRequest;
import project.toy.reservation.member.entity.Member;
import project.toy.reservation.member.repository.MemberRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private Member testMember;
    private final String email = "test@example.com";

    @BeforeEach
    void setUp() {
        testMember = Member.builder()
                .email(email)
                .password("encoded_old_password")
                .build();
    }

    @Test
    @DisplayName("회원가입 - 성공")
    void signUp_Success() {
        // given
        SignupRequest request = new SignupRequest();
        request.setEmail("new@example.com");
        request.setPassword("1234");

        given(memberRepository.existsByEmail(anyString())).willReturn(false);
        given(passwordEncoder.encode(anyString())).willReturn("encoded_1234");

        Member mockSavedMember = Member.builder().id(1L).email("new@example.com").build();
        given(memberRepository.save(any(Member.class))).willReturn(mockSavedMember);

        // when
        Member result = memberService.register(request);

        // then
        assertThat(result.getId()).isEqualTo(1L);
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("회원가입 성공 - 중복 이메일이 없는 경우")
    void signUp_Success_email() {
        // given
        SignupRequest request = createSignupRequest("new@test.com");

        given(memberRepository.existsByEmail(anyString())).willReturn(false);
        given(passwordEncoder.encode(anyString())).willReturn("encoded_password");

        Member savedMember = Member.builder().id(1L).email(request.getEmail()).build();
        given(memberRepository.save(any(Member.class))).willReturn(savedMember);

        // when
        Member result = memberService.register(request);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getEmail()).isEqualTo("new@test.com");
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("회원가입 실패 - 중복된 이메일이 있는 경우")
    void signUp_Fail_Duplicate() {
        // given
        SignupRequest request = createSignupRequest("dup@test.com");

        given(memberRepository.existsByEmail("dup@test.com")).willReturn(true);

        // when & then
        assertThatThrownBy(() -> memberService.register(request))
                .isInstanceOf(IllegalArgumentException.class);

        verify(passwordEncoder, never()).encode(anyString());
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    @DisplayName("마이페이지 조회 실패 - 사용자를 찾을 수 없음")
    void getMypage_Fail_NotFound() {
        // given
        String email = "none@example.com";
        given(memberRepository.findByEmail(email)).willReturn(Optional.empty()); // DB에 없음 가정

        // when & then
        assertThatThrownBy(() -> memberService.getMypage(email))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("사용자를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("비밀번호 검증 - 일치하면 true 반환")
    void verifyPassword_Success() {
        // given
        given(memberRepository.findByEmail(email)).willReturn(Optional.of(testMember));
        given(passwordEncoder.matches("1234", testMember.getPassword())).willReturn(true);

        // when
        boolean result = memberService.verifyPassword(email, "1234");

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("비밀번호 변경 - 성공")
    void updatePassword_Success() {
        // given
        given(memberRepository.findByEmail(email)).willReturn(Optional.of(testMember));
        given(passwordEncoder.encode("newPass")).willReturn("encoded_newPass");

        // when
        memberService.updatePassword(email, "newPass");

        // then
        verify(passwordEncoder, times(1)).encode("newPass");
        assertThat(testMember.getPassword()).isEqualTo("encoded_newPass");
    }

    private SignupRequest createSignupRequest(String email) {
        SignupRequest req = new SignupRequest();
        req.setEmail(email);
        req.setName("홍길동");
        req.setPassword("1234");
        return req;
    }
}
