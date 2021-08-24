package project.challengers.customizedRepo;

import project.challengers.entity.Auth;

import java.util.Date;
import java.util.List;

public interface CustomizedAuthRepository {
    List<Auth> getAuthFile(List<String> idList, long noticeSeq, Date date);
}
