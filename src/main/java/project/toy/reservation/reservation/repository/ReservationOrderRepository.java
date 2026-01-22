package project.toy.reservation.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.toy.reservation.reservation.entity.ReservationOrder;

import java.util.List;

public interface ReservationOrderRepository extends JpaRepository<ReservationOrder, Long> {
    List<ReservationOrder> findByMemberId(Long memberId);
}