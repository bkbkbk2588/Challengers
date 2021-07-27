package project.challengers.customizedRepo;

import project.challengers.DTO.notice.NoticeCreateDto;

public interface CustomizedNoticeRepository {
    int createNotice(NoticeCreateDto noticeCreate, String id);
}
