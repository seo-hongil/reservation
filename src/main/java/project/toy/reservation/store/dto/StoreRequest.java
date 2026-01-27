package project.toy.reservation.store.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import project.toy.reservation.member.entity.Member;
import project.toy.reservation.store.entity.Category;
import project.toy.reservation.store.entity.ReservationType;
import project.toy.reservation.store.entity.Store;

import java.time.LocalTime;
import java.util.List;

public class StoreRequest {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Register {
        @NotNull(message = "카테고리 선택은 필수입니다.")
        private Long categoryId;

        @NotBlank(message = "상점명은 필수 입력 항목입니다.")
        private String name;

        @NotBlank(message = "주소는 필수 입력 항목입니다.")
        private String address;

        @NotBlank(message = "상세주소는 필수 입력 항목입니다.")
        private String addressDetail;

        private String phone;

        @NotNull(message = "오픈 시간은 필수입니다.")
        private LocalTime openTime;

        @NotNull(message = "마감 시간은 필수입니다.")
        private LocalTime closeTime;

        private LocalTime breakStartTime;
        private LocalTime breakEndTime;
        private LocalTime lastOrderTime;
        private String dayOff;
        private String description;
        private String notice;

        @NotNull(message = "접수 방식은 최소 하나 이상 선택해야 합니다.")
        private List<ReservationType> reservationTypes;

        private List<String> options;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModifyRequest{
        @NotNull(message = "상점 ID는 필수입니다.")
        private Long id;

        @NotNull(message = "대분류 카테고리 선택은 필수입니다.")
        private Long parentCateId;

        @NotNull(message = "소분류 카테고리 선택은 필수입니다.")
        private Long childCateId;

        @NotBlank(message = "상점명은 필수 입력 항목입니다.")
        private String name;

        @NotBlank(message = "주소는 필수 입력 항목입니다.")
        private String address;

        @NotBlank(message = "상세주소는 필수 입력 항목입니다.")
        private String addressDetail;

        private String phone;

        @NotNull(message = "오픈 시간은 필수입니다.")
        private LocalTime openTime;

        @NotNull(message = "마감 시간은 필수입니다.")
        private LocalTime closeTime;

        private LocalTime breakStartTime;
        private LocalTime breakEndTime;
        private LocalTime lastOrderTime;
        private String dayOff;
        private String description;
        private String notice;

        @NotNull(message = "접수 방식은 최소 하나 이상 선택해야 합니다.")
        private List<ReservationType> reservationTypes;

        private List<String> options;

        private List<String> imageUrls;

        // 수정 시 화면에서 지우지 않고 유지하기로 한 기존 이미지 파일명들
        private List<String> keepImages;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SearchCond {
        private Long categoryId;
        private String keyword;
    }

    public static Store toNewStoreEntity(Register dto, Member member, Category category) {
        return Store.builder()
                .owner(member)
                .category(category)
                .name(dto.getName())
                .address(dto.getAddress())
                .addressDetail(dto.getAddressDetail())
                .phone(dto.getPhone())
                .openTime(dto.getOpenTime())
                .closeTime(dto.getCloseTime())
                .breakStartTime(dto.getBreakStartTime())
                .breakEndTime(dto.getBreakEndTime())
                .lastOrderTime(dto.getLastOrderTime())
                .dayOff(dto.getDayOff())
                .description(dto.getDescription())
                .notice(dto.getNotice())
                .options(dto.getOptions())
                .reservationType(dto.getReservationTypes())
                .build();
    }

    public static ModifyRequest toModifyDto(Store store) {
        return ModifyRequest.builder()
                .id(store.getId())
                .parentCateId(store.getCategory().getParent().getId())
                .childCateId(store.getCategory().getId())
                .name(store.getName())
                .address(store.getAddress())
                .addressDetail(store.getAddressDetail())
                .phone(store.getPhone())
                .openTime(store.getOpenTime())
                .closeTime(store.getCloseTime())
                .breakStartTime(store.getBreakStartTime())
                .breakEndTime(store.getBreakEndTime())
                .lastOrderTime(store.getLastOrderTime())
                .dayOff(store.getDayOff())
                .description(store.getDescription())
                .notice(store.getNotice())
                .options(store.getOptions())
                .reservationTypes(store.getReservationType())
                .imageUrls(store.getImageUrls())
                .build();
    }
}