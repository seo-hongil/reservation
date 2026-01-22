package project.toy.reservation.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.toy.reservation.reservation.entity.StoreReview;

public interface StoreReviewRepository extends JpaRepository<StoreReview, Long> {
}