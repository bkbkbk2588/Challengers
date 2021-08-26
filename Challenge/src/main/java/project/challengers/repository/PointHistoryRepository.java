package project.challengers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.challengers.entity.Member;
import project.challengers.entity.PointHistory;

import java.util.List;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
    List<PointHistory> findByMember(Member member);
    List<PointHistory> findByMemberAndStatusOrderByInsertTimeDesc(Member member, int status);
}
