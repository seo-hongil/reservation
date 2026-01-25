package project.toy.reservation.store.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationType {
    REMOTE("원격 줄서기"),
    FIELD("현장 접수"),
    BOOKING("사전 예약");

    private final String description;
}