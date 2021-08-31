package project.challengers.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.challengers.service.ChallengeService;

@Api(tags = {"도전방"})
@RestController
@RequestMapping("/challenge")
public class ChallengeController {

    @Autowired
    ChallengeService challengeService;

    @ApiOperation(value = "도전방 수동 시작")
    @PutMapping(value = "/start/{noticeSeq}")
    public int startChallenge(@ApiParam("도전방 번호") @PathVariable("noticeSeq") long noticeSeq, Authentication authentication) {
        return challengeService.startChallenge(noticeSeq, (String) authentication.getPrincipal());
    }

    @ApiOperation(value = "도전방 수동 종료")
    @PutMapping(value = "/stop/{noticeSeq}")
    public int stopChallenge(@ApiParam("도전방 번호") @PathVariable("noticeSeq") long noticeSeq, Authentication authentication) {
        return challengeService.stopChallenge(noticeSeq, (String) authentication.getPrincipal());
    }

    @ApiOperation(value = "포인트 분배")
    @PutMapping(value = "/point/{noticeSeq}")
    public int pointDistribution(@ApiParam("도전방 번호") @PathVariable("noticeSeq") long noticeSeq, Authentication authentication) {
        return challengeService.pointDistribution(noticeSeq, (String) authentication.getPrincipal());
    }

}
