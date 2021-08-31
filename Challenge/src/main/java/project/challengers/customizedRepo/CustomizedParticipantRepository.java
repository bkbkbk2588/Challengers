package project.challengers.customizedRepo;

import java.util.List;

public interface CustomizedParticipantRepository {
    int updateType(List<String> idList, long noticeSeq);
    int setCertification(List<String> idList, long noticeSeq);
    int setCredit(List<String> idList, long noticeSeq, int credit);
    int setBlind(long noticeSeq, String id);
    int setDelete(long noticeSeq, String id);
}
