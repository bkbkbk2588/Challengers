package project.challengers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.challengers.entity.NoticeFile;

import java.util.List;

public interface NoticeFileRepository extends JpaRepository<NoticeFile, Long> {
    List<NoticeFile> findByNoticeSeq(long noticeSeq);
}
