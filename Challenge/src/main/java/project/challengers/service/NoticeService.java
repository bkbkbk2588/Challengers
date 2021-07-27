package project.challengers.service;

import org.springframework.security.core.Authentication;
import project.challengers.DTO.notice.NoticeCreateDto;
import project.challengers.DTO.notice.NoticeListDto;

public interface NoticeService {
    NoticeListDto noticeList();
    int createNotice(NoticeCreateDto notice, Authentication authentication);
}
