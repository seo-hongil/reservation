package project.toy.reservation.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.toy.reservation.store.entity.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}