package project.challengers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.challengers.customizedRepo.CustomizedAuthRepository;
import project.challengers.entity.Auth;

import java.util.Date;
import java.util.List;

public interface AuthRepository extends JpaRepository<Auth, Long>, CustomizedAuthRepository {
    List<Auth> findByNoticeSeqAndAuthDate(long noticeSeq, Date authDate);
}
