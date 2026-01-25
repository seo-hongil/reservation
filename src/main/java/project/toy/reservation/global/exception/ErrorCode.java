package project.toy.reservation.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 공통
    INVALID_INPUT_VALUE("C001", "입력값이 올바르지 않습니다."),
    INTERNAL_SERVER_ERROR("C002", "서버 내부 오류입니다."),

    // 상점 관련
    STORE_NOT_FOUND("S001", "해당 상점을 찾을 수 없습니다."),
    DUPLICATE_STORE_NAME("S002", "이미 존재하는 상점 이름입니다."),
    CATEGORY_NOT_FOUND("S003", "카테고리가 존재하지 않습니다."),

    // 회원 관련
    MEMBER_NOT_FOUND("M001", "존재하지 않는 회원입니다.");

    private final String code;
    private final String message;
}
