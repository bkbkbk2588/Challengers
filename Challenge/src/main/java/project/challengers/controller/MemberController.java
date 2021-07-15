package project.challengers.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.challengers.service.MemberService;
import project.challengers.vo.MemberIdDupCheckVo;

@Api(tags={"사용자"})
@RestController
@RequestMapping("/member")
public class MemberController {
    Logger logger = LoggerFactory.getLogger(MemberController.class);

    @Autowired
    MemberService memberService;

    @ApiOperation(value="사용자아이디중복 확인", response=MemberIdDupCheckVo.class)
    @GetMapping(value="/dupcheck-id/{id}")
    public MemberIdDupCheckVo memberIdDupCheck(@ApiParam(value="사용자연락처", required=true) @PathVariable("id") String id) {
        return memberService.memberIdDupCheck(id);
    }
}
