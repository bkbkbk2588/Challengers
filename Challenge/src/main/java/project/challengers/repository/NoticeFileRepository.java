package project.challengers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.challengers.entity.NoticeFile;

public interface NoticeFileRepository extends JpaRepository<NoticeFile, Long> {
}
