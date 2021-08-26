package project.challengers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.challengers.customizedRepo.CustomizedApplyRepository;
import project.challengers.entity.Apply;
import project.challengers.entity.Notice;

import java.util.List;

public interface ApplyRepository extends JpaRepository<Apply, Long>, CustomizedApplyRepository {
    List<Apply> findByNotice(Notice notice);
}
