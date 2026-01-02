package project.toy.reservation.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.toy.reservation.member.dto.SignupRequest;
import project.toy.reservation.member.entity.Member;
import project.toy.reservation.member.entity.MemberStatus;
import project.toy.reservation.member.entity.RoleStatus;
import project.toy.reservation.member.repository.MemberRepository;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Member register(SignupRequest signupRequest){

        if(memberRepository.findByEmail(signupRequest.getEmail()).isPresent()){
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        Member member = Member.builder()
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .name(signupRequest.getName())
                .email(signupRequest.getEmail())
                .role(RoleStatus.USER)
                .status(MemberStatus.ACTIVE)
                .build();

        return memberRepository.save(member);
    }
}
