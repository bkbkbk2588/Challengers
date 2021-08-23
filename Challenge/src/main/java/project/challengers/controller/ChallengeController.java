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
import project.challengers.service.ChallengeService;
import reactor.core.publisher.Flux;

import java.io.IOException;

@Api(tags = {"도전방"})
@RestController
@RequestMapping("/challenge")
public class ChallengeController {
    Logger logger = LoggerFactory.getLogger(ChallengeController.class);

    /*
        TODO
            2. 인증 승인 or 경고주기도 가능 (방장만 권한 있음) put
            3. 수동 블라인드 처리 put
            4. 수동 강퇴 처리 put
            5. 방 수동 시작(시작시간이후부터만 가능) put
            6. 방 수동 종료(종료시간이후부터만 가능) put
            7. 포인트 분배 put
     */

    @Autowired
    ChallengeService challengeService;

    @ApiOperation(value = "출석 인증 파일 보내기")
    @PostMapping(value = "/auth", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public int attendanceAuth(@RequestPart("file") Flux<FilePart> filePartFlux,
                              @RequestPart("noticeSeq") String noticeSeq, Authentication authentication) {
        return challengeService.attendanceAuth(filePartFlux, Long.parseLong(noticeSeq), (String) authentication.getPrincipal());
    }

    @ApiOperation(value = "인증 파일 url 확인")
    @GetMapping(value = "/auth/file/{noticeSeq}",
            produces = {MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public AuthDto getAuthFile(@PathVariable("noticeSeq") long noticeSeq,
                               Authentication authentication, ServerHttpRequest req) {
        return challengeService.getAuthFile(noticeSeq, (String) authentication.getPrincipal(), req);
    }

    @ApiOperation(value = "인증 파일 조회 (인증 파일 조회에서 나온 url로 확인 가능)")
    @GetMapping(value = "/downloadFile/{fileName}",
            produces = {MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public byte[] downloadFile(@ApiParam(value = "사진 제목") @PathVariable String fileName) throws IOException {
        return challengeService.downloadFile(fileName);
    }
}
