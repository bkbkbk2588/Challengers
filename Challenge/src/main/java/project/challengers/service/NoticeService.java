package project.challengers.service;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import project.challengers.DTO.notice.NoticeCreateDto;
import project.challengers.DTO.notice.NoticeInfoDto;
import project.challengers.DTO.notice.NoticeListDto;
import project.challengers.DTO.notice.SearchPagingDto;
import reactor.core.publisher.Flux;

import java.io.IOException;

public interface NoticeService {
    NoticeListDto noticeList();
    int createNotice(NoticeCreateDto notice, Flux<FilePart> filePartFlux, Authentication authentication);
    NoticeListDto noticePagingList(SearchPagingDto paging);
    NoticeInfoDto getNotice(long noticeSeq, ServerHttpRequest req);
    byte[] downloadFile(String fileName) throws IOException;
}
