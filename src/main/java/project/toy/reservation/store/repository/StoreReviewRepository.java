package project.toy.reservation.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.toy.reservation.store.entity.StoreReview;

public interface StoreReviewRepository extends JpaRepository<StoreReview, Long> {
}