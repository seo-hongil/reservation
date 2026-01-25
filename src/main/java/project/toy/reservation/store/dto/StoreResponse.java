package project.toy.reservation.store.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreResponse {

    private Long no;                    // 상세 페이지 이동을 위한 ID
    private String category;            // FOOD, BAR, BAKERY 구분
    private String name;                // 상호명
    private String order_possible;      // 간단한 설명
    private String imageUrl;            // 썸네일 경로
    private Integer review_scroe;       // 총 리뷰점수
    private Integer review_total_count; // 총 리뷰개수
    private String type;                // 음식 종류(한식,양식,일식)

}