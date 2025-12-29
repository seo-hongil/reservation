package project.toy.reservation.member.entity;

public enum MemberStatus {
    ACTIVE,        // 정상회원
    INACTIVE,      // 이메일 미인증 (초기가입상태)
    SUSPENDED,     // 이용정지된 계정
    WITHDRAWN      // 탈퇴회원
}
