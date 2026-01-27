package project.toy.reservation.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.toy.reservation.store.entity.Store;

public interface StoreRepository extends JpaRepository<Store, Long>, StoreRepositoryCustom {
}