package project.challengers.service;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.core.Authentication;
import project.challengers.DTO.notice.NoticeCreateDto;
import project.challengers.DTO.notice.NoticeListDto;
import project.challengers.DTO.notice.SearchPagingDto;
import reactor.core.publisher.Flux;

public interface NoticeService {
    NoticeListDto noticeList();
    int createNotice(NoticeCreateDto notice, Flux<FilePart> filePartFlux, Authentication authentication);
    NoticeListDto noticePagingList(SearchPagingDto paging);
}
