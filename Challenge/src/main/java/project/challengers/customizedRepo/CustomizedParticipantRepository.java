package project.challengers.customizedRepo;

import java.util.List;

public interface CustomizedParticipantRepository {
    int updateType(List<String> idList, long noticeSeq, int type);
}
