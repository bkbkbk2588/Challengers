package project.challengers.customizedRepo;

import com.querydsl.core.Tuple;
import project.challengers.DTO.notice.NoticeInfoDto;
import project.challengers.DTO.notice.NoticeListDto;
import project.challengers.DTO.notice.SearchPagingDto;
import project.challengers.entity.Notice;

import java.util.List;

public interface CustomizedNoticeRepository {
    NoticeListDto noticePagingList(SearchPagingDto paging);
    List<Tuple> getNotice(long noticeSeq);
}
