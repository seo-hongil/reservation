package project.toy.reservation.reservation.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import project.toy.reservation.member.entity.Member;
import project.toy.reservation.store.entity.Store;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "waiting")
public class Waiting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "waiting_number", nullable = false)
    private Integer waitingNumber;

    @Column(name = "conf_code", length = 10)
    private String confCode;

    @Column(name = "is_confirmed", length = 1)
    @Builder.Default
    private String isConfirmed = "N";

    @Column(length = 20)
    private String status;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "call_time")
    private LocalDateTime callTime;

    @Column(name = "arrival_time")
    private LocalDateTime arrivalTime;

}
