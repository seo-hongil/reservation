package project.toy.reservation.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.toy.reservation.reservation.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}