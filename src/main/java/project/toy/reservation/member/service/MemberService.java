package project.toy.reservation.member.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.toy.reservation.member.dto.MypageDto;
import project.toy.reservation.member.dto.SignupRequest;
import project.toy.reservation.member.entity.Member;
import project.toy.reservation.member.entity.MemberStatus;
import project.toy.reservation.member.entity.RoleStatus;
import project.toy.reservation.member.repository.MemberRepository;
import project.toy.reservation.reservation.entity.*;

import java.util.List;
import java.util.NoSuchElementException;

import static project.toy.reservation.reservation.entity.QReservationOrder.reservationOrder;
import static project.toy.reservation.reservation.entity.QStore.store;
import static project.toy.reservation.reservation.entity.QWaiting.waiting;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final JPAQueryFactory queryFactory;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public Member register(SignupRequest signupRequest){

        if(memberRepository.existsByEmail(signupRequest.getEmail())){
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        Member member = Member.builder()
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .name(signupRequest.getName())
                .email(signupRequest.getEmail())
                .phone(signupRequest.getPhone())
                .role(RoleStatus.USER)
                .status(MemberStatus.ACTIVE)
                .build();

        return memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public MypageDto getMypage(String email){
        Member userInfo = memberRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));

        List<Waiting> waitings = queryFactory
                .selectFrom(waiting)
                .join(waiting.store, store).fetchJoin()
                .where(waiting.member.eq(userInfo), waiting.status.in("WAIT", "CONFIRMED"))
                .orderBy(waiting.createdAt.desc())
                .fetch();

        List<ReservationOrder> reservations = queryFactory
                .selectFrom(reservationOrder)
                .join(reservationOrder.store, store).fetchJoin()
                .where(reservationOrder.member.eq(userInfo))
                .orderBy(reservationOrder.reservedDate.asc(), reservationOrder.reservedTime.asc())
                .fetch();

        List<Store> stores = queryFactory
                .selectFrom(store)
                .join(store.owner).fetchJoin()
                .where(store.owner.eq(userInfo))
                .orderBy(store.createdAt.asc())
                .fetch();

        return MypageDto.builder()
              .currentWaitings(waitings.stream().map(MypageDto::toWaitingDto).toList())
              .currentReservationOrders(reservations.stream().map(MypageDto::toReservationDto).toList())
              .myStores(stores.stream().map(MypageDto::toStoreDto).toList())
              .profile(MypageDto.toProfileDto(userInfo))
              .build();
    }

    @Transactional(readOnly = true)
    public boolean verifyPassword(String email, String rawPassword) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));

        return passwordEncoder.matches(rawPassword, member.getPassword());
    }

    @Transactional
    public void updatePassword(String email, String newPassword) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));

        String encodedPassword = passwordEncoder.encode(newPassword);
        member.updatePassword(encodedPassword);
    }
}