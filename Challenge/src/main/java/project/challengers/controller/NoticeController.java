package project.challengers.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import project.challengers.DTO.notice.NoticeCreateDto;
import project.challengers.DTO.notice.NoticeInfoDto;
import project.challengers.DTO.notice.NoticeListDto;
import project.challengers.DTO.notice.SearchPagingDto;
import project.challengers.service.NoticeService;
import reactor.core.publisher.Flux;

import java.io.IOException;

@Api(tags = {"도전 게시글"})
@RestController
@RequestMapping("/notice")
public class NoticeController {
    /* TODO
        1. 게시글 개수 필터 조회
        2. 게시글 제목으로 검색
        3. 게시글 내용으로 검색
        5. 도전 게시글 삭제
        6. 도전 게시글 수정 (작성자만 권한 있음) (첨부파일이 있는거랑 없는거 둘다)
     */

    Logger logger = LoggerFactory.getLogger(NoticeController.class);

    @Autowired
    NoticeService noticeService;

    @Autowired
    ObjectMapper objectMapper;

    @ApiOperation(value = "게시글 전체 목록 조회", response = NoticeListDto.class)
    @GetMapping(value = "/list")
    public NoticeListDto noticeList() {
        return noticeService.noticeList();
    }

    @ApiOperation(value = "게시글 등록 (첨부파일 X)")
    @PostMapping(value = "/create")
    public int createNotice(@ApiParam("게시글 등록") @RequestBody NoticeCreateDto notice, Authentication authentication) {
        return noticeService.createNotice(notice, null, authentication);
    }

    @ApiOperation(value = "게시글 등록 (첨부파일 O)")
    @PostMapping(value = "/create/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public int createFileNotice(@RequestPart("notice") String notice,
                                @RequestPart("files") Flux<FilePart> filePartFlux, Authentication authentication)
            throws JsonProcessingException {
        NoticeCreateDto noticeCreateDto = objectMapper.readValue(notice, NoticeCreateDto.class);

        return noticeService.createNotice(noticeCreateDto, filePartFlux, authentication);
    }

    @ApiOperation(value = "게시글 페이지 조회")
    @GetMapping(value = "/list/page")
    public NoticeListDto noticePagingList(@ApiParam(value = "검색페이징") SearchPagingDto paging) {
        return null;
    }

    @ApiOperation(value = "게시글 조회", response = NoticeInfoDto.class)
    @GetMapping(value = "/{seq}")
    public NoticeInfoDto getNotice(@ApiParam(value = "게시글 번호") @PathVariable long seq,
                                   ServerHttpRequest request) {
        return noticeService.getNotice(seq, request);
    }

    @ApiOperation(value = "게시글 사진 조회 (게시글 조회에서 나온 url로 확인 가능)")
    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@ApiParam(value = "사진 제목") @PathVariable String fileName) throws IOException {
        return noticeService.downloadFile(fileName);
    }
}
