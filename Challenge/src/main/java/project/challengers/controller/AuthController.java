package project.challengers.controller;

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
import project.challengers.DTO.auth.AuthDto;
import project.challengers.service.AuthService;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.List;

@Api(tags = {"인증"})
@RestController
@RequestMapping("/auth")
public class AuthController {
    Logger logger = LoggerFactory.getLogger(AuthController.class);

    /*
        TODO
            3. 수동 블라인드 처리 put participant
            4. 수동 강퇴 처리 put participant
            5. 방 수동 시작(시작시간이후부터만 가능) put challenge
            6. 방 수동 종료(종료시간이후부터만 가능) put challenge
            7. 포인트 분배 put challenge
     */

    @Autowired
    AuthService authService;

    @ApiOperation(value = "출석 인증 파일 보내기")
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public int attendanceAuth(@RequestPart("file") Flux<FilePart> filePartFlux,
                              @RequestPart("noticeSeq") String noticeSeq, Authentication authentication) {
        return authService.attendanceAuth(filePartFlux, Long.parseLong(noticeSeq), (String) authentication.getPrincipal());
    }

    @ApiOperation(value = "인증 파일 url 확인")
    @GetMapping(value = "/file/{noticeSeq}",
            produces = {MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public AuthDto getAuthFile(@PathVariable("noticeSeq") long noticeSeq,
                               Authentication authentication, ServerHttpRequest req) {
        return authService.getAuthFile(noticeSeq, (String) authentication.getPrincipal(), req);
    }

    @ApiOperation(value = "인증 파일 조회 (인증 파일 조회에서 나온 url로 확인 가능)")
    @GetMapping(value = "/downloadFile/{fileName}",
            produces = {MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public byte[] downloadFile(@ApiParam(value = "사진 제목") @PathVariable String fileName) throws IOException {
        return authService.downloadFile(fileName);
    }

    @ApiOperation(value = "인증 미승인 사용자 경고")
    @PutMapping(value = "/{noticeSeq}")
    public int setCertification(@ApiParam("도전방 번호") @PathVariable("noticeSeq") long noticeSeq,
                                @ApiParam("승인 아이디 리스트") @RequestBody List<String> idList, Authentication authentication) {
        return authService.setCertification(noticeSeq, idList, (String) authentication.getPrincipal());
    }

    // 벌금 조회로 변경
    @ApiOperation(value = "인증 삭제(사용 X)")
    @DeleteMapping(value = "/{noticeSeq}")
    public int deleteAuth(Authentication authentication) {
        return 0;
    }
}
