package project.challengers.customizedRepo;

import com.querydsl.core.Tuple;
import project.challengers.entity.Auth;

import java.util.List;

public interface CustomizedAuthRepository {
    List<Auth> getAuthFile(List<String> idList, long noticeSeq);
}
