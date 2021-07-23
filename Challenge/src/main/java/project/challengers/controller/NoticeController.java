package project.challengers.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"도전 게시글"})
@RestController
@RequestMapping("/notice")
public class NoticeController {
    /* TODO
        1. notice에서 이미지 테이블로 빼서 다시 조정
        2. 도전 게시글 목록 조회
        3. 도전 게시글 상세 조회
        4. 도전 게시글 등록
        5. 도전 게시글 삭제
        6. 도전 게시글 수정 (작성자만 권한 있음)
        7. 등록에 세부 사항 검토 필요
     */
}
