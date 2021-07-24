package project.challengers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.challengers.customizedRepo.CustomizedNoticeRepository;
import project.challengers.entity.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long>, CustomizedNoticeRepository {
}
