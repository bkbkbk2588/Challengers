package project.challengers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.challengers.entity.PointHistory;

import java.util.List;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
    List<PointHistory> findById(String id);
    List<PointHistory> findByIdAndStatusOrderByInsertTimeDesc(String id, int status);
}
