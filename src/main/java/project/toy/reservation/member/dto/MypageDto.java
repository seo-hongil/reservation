package project.toy.reservation.member.dto;

import lombok.Builder;
import lombok.Getter;
import project.toy.reservation.member.entity.Member;
import project.toy.reservation.reservation.entity.ReservationOrder;
import project.toy.reservation.store.entity.Store;
import project.toy.reservation.reservation.entity.Waiting;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Builder
public class MypageDto {

    private List<WaitingDto> currentWaitings;
    private List<ReservationOrderDto> currentReservationOrders;
    private List<StoreDto> myStores;
    private ProfileDto profile;

    @Getter @Builder
    public static class WaitingDto{
        private Long id;
        private String storeName;
        private int waitingNumber;
        private String status;
    }

    @Getter @Builder
    public static class ReservationOrderDto{
        private Long id;
        private String storeName;
        private LocalDate reservedDate;
        private LocalTime reservedTime;
        private String status;
    }

    @Getter @Builder
    public static class StoreDto{
        private Long id;
        private String name;
    }

    @Getter @Builder
    public static class ProfileDto{
        private Long id;
        private String name;
        private String email;
        private String phone;
    }

    public static WaitingDto toWaitingDto(Waiting w){
        return WaitingDto.builder()
                .id(w.getId())
                .storeName(w.getStore().getName())
                .waitingNumber(w.getWaitingNumber())
                .status(w.getStatus())
                .build();
    }

    public static ReservationOrderDto toReservationDto(ReservationOrder r) {
        return ReservationOrderDto.builder()
                .id(r.getId())
                .storeName(r.getStore().getName())
                .reservedDate(r.getReservedDate())
                .reservedTime(r.getReservedTime())
                .status(r.getStatus())
                .build();
    }

    public static StoreDto toStoreDto(Store s){
        return StoreDto.builder()
                .id(s.getId())
                .name(s.getName())
                .build();
    }

    public static ProfileDto toProfileDto(Member m){
        return ProfileDto.builder()
                .id(m.getId())
                .name(m.getName())
                .email(m.getEmail())
                .phone(m.getPhone())
                .build();
    }
}
