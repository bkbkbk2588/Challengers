package project.challengers.service;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import project.challengers.DTO.notice.*;
import reactor.core.publisher.Flux;

import java.io.IOException;

public interface NoticeService {
    NoticeListDto noticeList();
    int createNotice(NoticeCreateDto notice, Flux<FilePart> filePartFlux, Authentication authentication);
    NoticeListDto noticePagingList(SearchPagingDto paging);
    NoticeInfoDto getNotice(long noticeSeq, ServerHttpRequest req);
    byte[] downloadFile(String fileName) throws IOException;
    NoticeListDto noticeSearchTitle(String title, SearchPagingDto paging);
    NoticeListDto noticeSearchContent(String content, SearchPagingDto paging);
    int deleteNotice(long noticeSeq, Authentication authentication);
    int updateNotice(NoticeUpdateDto notice, Flux<String> fileSeq, Flux<FilePart> filePartFlux, Authentication authentication);
}
