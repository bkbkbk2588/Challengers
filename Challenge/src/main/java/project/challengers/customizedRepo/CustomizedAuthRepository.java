package project.challengers.customizedRepo;

import project.challengers.entity.Auth;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface CustomizedAuthRepository {
    List<Auth> getAuthFile(List<String> idList, long noticeSeq, LocalDate date);
    void deleteAuthFile(List<Long> authSeqList);
}
