package project.challengers.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import project.challengers.DTO.apply.ApplyDto;
import project.challengers.DTO.apply.ApplyListDto;
import project.challengers.service.ApplyService;

import java.util.List;

@Api(tags = {"도전 신청"})
@RestController
@RequestMapping("/apply")
public class ApplyController {
    Logger logger = LoggerFactory.getLogger(ApplyController.class);

    @Autowired
    ApplyService applyService;

    @ApiOperation(value = "도전 신청하기")
    @PostMapping(value = "/challenge")
    public int insertApply(@RequestBody ApplyDto apply, Authentication authentication) {
        return applyService.insertApply(apply, (String) authentication.getPrincipal());
    }

    @ApiOperation(value = "도전 신청 수락(방장만 가능)")
    @DeleteMapping(value = "/accept/{noticeSeq}")
    public int acceptApply(@ApiParam("도전방 번호") @PathVariable long noticeSeq,
                           @ApiParam("id 리스트") @RequestBody List<String> idList, Authentication authentication) {
        return applyService.acceptApply(noticeSeq, idList, (String) authentication.getPrincipal());
    }

    @ApiOperation(value = "도전방 신청자 목록")
    @GetMapping(value = "/list/{noticeSeq}")
    public List<ApplyListDto> getApplyList(@ApiParam("도전방 번호") @PathVariable("noticeSeq") long noticeSeq,
                                           Authentication authentication) {
        return applyService.getApplyList(noticeSeq, (String) authentication.getPrincipal());
    }
}
