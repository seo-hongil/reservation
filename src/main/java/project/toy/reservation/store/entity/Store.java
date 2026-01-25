package project.toy.reservation.store.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import project.toy.reservation.member.entity.Member;
import project.toy.reservation.store.dto.StoreRequest.ModifyRequest;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "store")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "image_urls", columnDefinition = "json")
    private List<String> imageUrls;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String addressDetail;

    private String phone;

    @Column(name = "open_time", nullable = false)
    private LocalTime openTime;

    @Column(name = "close_time", nullable = false)
    private LocalTime closeTime;

    @Column(name = "break_start_time")
    private LocalTime breakStartTime;

    @Column(name = "break_end_time")
    private LocalTime breakEndTime;

    @Column(name = "last_order_time")
    private LocalTime lastOrderTime;

    @Column(name = "day_off", length = 50)
    private String dayOff;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String notice;

    @Column(name = "store_url")
    private String storeUrl;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "reservation_type", nullable = false)
    private List<ReservationType>  reservationType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private List<String> options;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public void updateThumbnailFromImages(String imageUrls) {
            this.thumbnailUrl = imageUrls;
    }

    public void addImages(List<String> images){
        this.imageUrls = images;
    }

    public void updateImages(List<String> images) {
        this.imageUrls = images;

        if (images != null && !images.isEmpty()) {
            this.thumbnailUrl = images.get(0);
        } else {
            this.thumbnailUrl = "noImage";
        }
    }

    public void modifyRequestDetails(ModifyRequest dto, Category category) {
        this.category = category;
        this.name = dto.getName();
        this.address = dto.getAddress();
        this.addressDetail = dto.getAddressDetail();
        this.phone = dto.getPhone();
        this.openTime = dto.getOpenTime();
        this.closeTime = dto.getCloseTime();
        this.breakStartTime = dto.getBreakStartTime();
        this.breakEndTime = dto.getBreakEndTime();
        this.lastOrderTime = dto.getLastOrderTime();
        this.dayOff = dto.getDayOff();
        this.description = dto.getDescription();
        this.notice = dto.getNotice();
        this.reservationType = dto.getReservationTypes();
        this.options = dto.getOptions();
    }
}
