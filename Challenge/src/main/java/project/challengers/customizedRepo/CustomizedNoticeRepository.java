package project.challengers.customizedRepo;

import com.querydsl.core.Tuple;
import org.aspectj.weaver.ast.Not;
import project.challengers.DTO.notice.NoticeInfoDto;
import project.challengers.DTO.notice.NoticeListDto;
import project.challengers.DTO.notice.NoticeUpdateDto;
import project.challengers.DTO.notice.SearchPagingDto;
import project.challengers.entity.Notice;
import project.challengers.entity.NoticeFile;

import java.util.List;

public interface CustomizedNoticeRepository {
    List<Notice> findNoticeAll();
    List<Notice> noticePagingList(SearchPagingDto paging);
    List<Tuple> getNotice(long noticeSeq);
    List<Notice> noticeSearchTitle(String title, SearchPagingDto paging);
    List<Notice> noticeSearchContent(String content, SearchPagingDto paging);
    int deleteNotice(long noticeSeq);
    int deleteNoticeFile(long noticeSeq);
    int deleteFile(List<Long> fileSeq);
    List<String> findByFilePath(List<Long> fileSeq);
    int updateNotice(NoticeUpdateDto noticeUpdateDto);
}
