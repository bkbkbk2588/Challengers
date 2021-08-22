package project.challengers.service;

import project.challengers.DTO.apply.ApplyDto;
import project.challengers.DTO.apply.ApplyListDto;

import java.util.List;

public interface ApplyService {
    int insertApply(ApplyDto apply, String id);
    int acceptApply(long noticeSeq, List<String> idList, String id);
    List<ApplyListDto> getApplyList(long noticeSeq, String id);
}
