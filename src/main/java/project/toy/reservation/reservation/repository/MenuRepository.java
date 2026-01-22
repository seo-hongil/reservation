package project.toy.reservation.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.toy.reservation.reservation.entity.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}