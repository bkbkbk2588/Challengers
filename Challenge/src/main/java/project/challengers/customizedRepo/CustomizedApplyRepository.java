package project.challengers.customizedRepo;

import java.util.List;

public interface CustomizedApplyRepository {
    int acceptApply(List<String> id, long noticeSeq);
}
