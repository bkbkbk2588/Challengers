package project.challengers.service;

import org.springframework.security.core.Authentication;
import project.challengers.DTO.notice.FileNoticeCreateDto;
import project.challengers.DTO.notice.NoticeCreateDto;
import project.challengers.DTO.notice.NoticeListDto;
import project.challengers.DTO.notice.SearchPagingDto;

public interface NoticeService {
    NoticeListDto noticeList();
    int createNotice(NoticeCreateDto notice, Authentication authentication);
    int createFileNotice(FileNoticeCreateDto notice, Authentication authentication);
    NoticeListDto noticePagingList(SearchPagingDto paging);
}
