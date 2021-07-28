package project.challengers.customizedRepo;

import project.challengers.DTO.notice.NoticeListDto;
import project.challengers.DTO.notice.SearchPagingDto;

public interface CustomizedNoticeRepository {
    NoticeListDto noticePagingList(SearchPagingDto paging);
}
