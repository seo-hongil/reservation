package project.toy.reservation.member.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import project.toy.reservation.member.dto.MypageDto;
import project.toy.reservation.member.entity.Member;
import project.toy.reservation.member.entity.MemberStatus;
import project.toy.reservation.member.entity.RoleStatus;
import project.toy.reservation.member.repository.MemberRepository;
import project.toy.reservation.reservation.entity.Category;
import project.toy.reservation.reservation.entity.ReservationType;
import project.toy.reservation.reservation.entity.Store;
import project.toy.reservation.reservation.entity.Waiting;
import project.toy.reservation.reservation.repository.CategoryRepository;
import project.toy.reservation.reservation.repository.StoreRepository;
import project.toy.reservation.reservation.repository.WaitingRepository;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class MemberServiceIntegrationTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private WaitingRepository waitingRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private Member testMember;

    @BeforeEach
    void setUp() {
        // 1. 테스트용 회원 생성
        testMember = Member.builder()
                .email("test@example.com")
                .name("홍길동")
                .password(passwordEncoder.encode("1234"))
                .phone("010-1234-5678")
                .role(RoleStatus.USER)
                .status(MemberStatus.ACTIVE)
                .build();
        memberRepository.save(testMember);

        // 2. 테스트용 카테고리 생성
        Category category = Category.builder()
                .name("한식")
                .build();

        categoryRepository.save(category);

        // 3. 테스트용 가게 생성
        Store store = Store.builder()
                .name("맛있는 식당")
                .owner(testMember)
                .category(category)
                .address("서울시 강남구")
                .openTime(LocalTime.now())
                .closeTime(LocalTime.MAX)
                .reservationType(ReservationType.REMOTE)
                .build();
        storeRepository.save(store);

        // 4. 테스트용 웨이팅 데이터 생성
        Waiting waiting = Waiting.builder()
                .member(testMember)
                .store(store)
                .waitingNumber(5)
                .status("WAIT")
                .build();
        waitingRepository.save(waiting);
    }

    @Test
    @DisplayName("이메일로 사용자의 마이페이지 정보를 조회하면 웨이팅과 예약 내역이 반환된다")
    void getMyPageInfoTest() {
        // given
        String email = "test@example.com";

        // when
        MypageDto response = memberService.getMypage(email);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getCurrentWaitings()).hasSize(1);
        assertThat(response.getCurrentWaitings().get(0).getStoreName()).isEqualTo("맛있는 식당");
        assertThat(response.getCurrentWaitings().get(0).getWaitingNumber()).isEqualTo(5);
    }

    @Test
    @DisplayName("비밀번호 변경 성공 - 실제 DB 반영 확인")
    void changePassword_Success_Integration() {
        // when
        String newPassword = "new12345";
        memberService.updatePassword("test@example.com", newPassword);

        // then
        Member updatedMember = memberRepository.findByEmail("test@example.com")
                .orElseThrow(() -> new AssertionError("회원이 존재해야 합니다."));

        assertThat(passwordEncoder.matches(newPassword, updatedMember.getPassword())).isTrue();
        assertThat(passwordEncoder.matches("1234", updatedMember.getPassword())).isFalse();
    }
}