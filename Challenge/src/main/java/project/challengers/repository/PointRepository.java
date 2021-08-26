package project.challengers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.challengers.customizedRepo.CustomizedPointRepository;
import project.challengers.entity.Member;
import project.challengers.entity.Point;

public interface PointRepository extends JpaRepository<Point, Long>, CustomizedPointRepository {
    Point findByMember(Member member);
}
