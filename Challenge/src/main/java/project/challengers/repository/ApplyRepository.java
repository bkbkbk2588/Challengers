package project.challengers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.challengers.customizedRepo.CustomizedApplyRepository;
import project.challengers.entity.Apply;

import java.util.List;

public interface ApplyRepository extends JpaRepository<Apply, Long>, CustomizedApplyRepository {
    List<Apply> findByNoticeSeq(long noticeSeq);
}
