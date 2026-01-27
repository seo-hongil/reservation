package project.toy.reservation.store.dto;

import lombok.*;
import project.toy.reservation.store.entity.Category;
import project.toy.reservation.store.entity.ReservationType;
import project.toy.reservation.store.entity.Store;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StoreResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class StoreList {
        private List<StoreInfo> stores;          // 상점 목록
        private List<CategoryDto> categories;    // 상단 카테고리 탭 리스트
        private Long selectedCategoryId;         // 현재 체크된 카테고리
        private String keyword;                  // 입력했던 검색어
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StoreInfo {
        private Long id;                    // 상세 페이지 이동을 위한 ID
        private Category category;            // FOOD, BAR, BAKERY 구분
        private String name;                // 상호명
        private String thumbnailUrl;            // 썸네일 경로
        private List<ReservationType> type;                // 음식 종류(한식,양식,일식)
        private String address;
        //private Integer review_scroe;       // 총 리뷰점수
        //private Integer review_total_count; // 총 리뷰개수
    }

    public static StoreInfo toStoreInfoDto(Store store){
        return StoreInfo.builder()
                .id(store.getId())
                .category(store.getCategory())
                .name(store.getName())
                .thumbnailUrl(store.getThumbnailUrl())
                .type(store.getReservationType())
                .address(extractDong(store.getAddress()))
                .build();
    }


    private static String extractDong(String address) {
        if (address == null || address.isBlank()) return "";

        // 정규표현식: '동', '읍', '면', '가'로 끝나는 단어를 찾음 (예: 주교동, 연무읍, 가산면, 종로3가)
        Pattern pattern = Pattern.compile("([^\\s]+(?:동|읍|면|가|로\\d+가))");
        Matcher matcher = pattern.matcher(address);

        if (matcher.find()) {
            return matcher.group(1);
        }

        // 정규식으로 못 찾을 경우, 공백으로 잘라 3번째 단어 반환 (일반적인 지번/도로명 주소 대비)
        String[] parts = address.split(" ");
        return (parts.length >= 3) ? parts[2] : parts[parts.length - 1];
    }
}