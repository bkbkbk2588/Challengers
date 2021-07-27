package project.challengers.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import project.challengers.DTO.notice.NoticeCreateDto;
import project.challengers.DTO.notice.NoticeListDto;
import project.challengers.service.NoticeService;

@Api(tags = {"도전 게시글"})
@RestController
@RequestMapping("/notice")
public class NoticeController {
    /* TODO
        1. 도전 게시글 등록 (첨부파일 X)
        2. 도전 게시글 등록 (첨부파일 O)
        3. 게시글 개수 필터 조회
        4. 게시글 제목으로 검색
        5. 게시글 내용으로 검색
        6. 도전 게시글 상세 조회
        7. 도전 게시글 삭제
        8. 도전 게시글 수정 (작성자만 권한 있음) (첨부파일이 있는거랑 없는거 둘다)
     */

    Logger logger = LoggerFactory.getLogger(NoticeController.class);

    @Autowired
    NoticeService noticeService;

    @ApiOperation(value = "게시글 전체 목록 조회")
    @GetMapping(value = "/list")
    public NoticeListDto noticeList() {
        return noticeService.noticeList();
    }

    @ApiOperation(value = "게시글 등록 (첨부파일 X)")
    @PostMapping(value = "/create")
    public int createNotice(@ApiParam("게시글 등록") @RequestBody NoticeCreateDto notice, Authentication authentication) {

        return noticeService.createNotice(notice, authentication);
    }

}
