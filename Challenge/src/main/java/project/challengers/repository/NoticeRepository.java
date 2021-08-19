package project.challengers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.challengers.customizedRepo.CustomizedNoticeRepository;
import project.challengers.entity.Notice;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long>, CustomizedNoticeRepository {
}
