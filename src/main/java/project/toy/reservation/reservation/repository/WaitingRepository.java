package project.toy.reservation.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.toy.reservation.reservation.entity.Waiting;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {
}