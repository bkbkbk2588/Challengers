package project.challengers.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import project.challengers.DTO.notice.*;
import project.challengers.service.NoticeService;
import reactor.core.publisher.Flux;

import java.io.IOException;

@Api(tags = {"도전 게시글"})
@RestController
@RequestMapping("/notice")
public class NoticeController {
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
        return noticeService.noticePagingList(paging);
    }

    @ApiOperation(value = "게시글 조회", response = NoticeInfoDto.class)
    @GetMapping(value = "/{seq}")
    public NoticeInfoDto getNotice(@ApiParam(value = "게시글 번호") @PathVariable long seq,
                                   ServerHttpRequest request) {
        return noticeService.getNotice(seq, request);
    }

    @ApiOperation(value = "게시글 사진 조회 (게시글 조회에서 나온 url로 확인 가능)")
    @GetMapping(value = "/downloadFile/{fileName}",
            produces = {MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public byte[] downloadFile(@ApiParam(value = "사진 제목") @PathVariable String fileName) throws IOException {
        return noticeService.downloadFile(fileName);
    }

    @ApiOperation(value = "게시글 제목 검색(Like 검색)")
    @GetMapping(value = "/searchTitle/{title}")
    public NoticeListDto noticeSearchTitle(@ApiParam(value = "게시글 제목") @PathVariable(value = "title") String title,
                                           @ApiParam(value = "검색페이징") SearchPagingDto paging) {
        return noticeService.noticeSearchTitle(title, paging);
    }

    @ApiOperation(value = "게시글 내용 검색(Like 검색)")
    @GetMapping(value = "/searchContent/{content}")
    public NoticeListDto noticeSearchContent(@ApiParam(value = "게시글 내용") @PathVariable String content,
                                             @ApiParam(value = "검색페이징") SearchPagingDto paging) {
        return noticeService.noticeSearchContent(content, paging);
    }

    @ApiOperation(value = "게시글 삭제")
    @DeleteMapping(value = "/delete/{noticeSeq}")
    public int deleteNotice(@ApiParam(value = "게시글 번호") @PathVariable(value = "noticeSeq") long noticeSeq, Authentication authentication) {
        return noticeService.deleteNotice(noticeSeq, authentication);
    }

    @ApiOperation(value = "게시글 수정 (첨부파일 수정 X)")
    @PutMapping(value = "/update")
    public int updateNotice(@ApiParam(value = "게시글 번호") @RequestBody NoticeUpdateDto notice, Authentication authentication) {
        return noticeService.updateNotice(notice, null, null, authentication);
    }

    @ApiOperation(value = "게시글 수정 (첨부파일 수정 O)")
    @PutMapping(value = "/update/file", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public int updateNoticeFile(@RequestPart("notice") String notice, @RequestPart("deleteFiles") Flux<String> fileSeq,
                                @RequestPart("Files") Flux<FilePart> filePartFlux, Authentication authentication) throws JsonProcessingException {
        NoticeUpdateDto noticeUpdateDto = objectMapper.readValue(notice, NoticeUpdateDto.class);
        return noticeService.updateNotice(noticeUpdateDto, fileSeq, filePartFlux, authentication);
    }
}
