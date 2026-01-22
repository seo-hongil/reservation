package project.toy.reservation.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.toy.reservation.reservation.entity.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {
}