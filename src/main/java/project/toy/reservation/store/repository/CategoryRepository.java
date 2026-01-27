package project.toy.reservation.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.toy.reservation.store.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByDepth(Integer depth);
    List<Category> findAllByParentId(Long parentId);
    Optional<Category> findByName(String name);
    Optional<Category> findById(Long Id);
}