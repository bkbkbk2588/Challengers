package project.challengers.customizedRepo;

import java.util.List;

public interface CustomizedParticipantRepository {
    int updateType(List<String> idList, long noticeSeq, int type);
    int setCertification(List<String> idList, long noticeSeq);
    int setCredit(List<String> idList, long noticeSeq, int credit);
}
