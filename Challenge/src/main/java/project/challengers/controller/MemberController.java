package project.challengers.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.challengers.DTO.member.LoginResponseDTO;
import project.challengers.DTO.member.MemberIdDupCheckDTO;
import project.challengers.DTO.member.MemberLoginDTO;
import project.challengers.DTO.member.MemberSignUpDto;
import project.challengers.service.MemberService;

@Api(tags={"사용자"})
@RestController
@RequestMapping("/member")
public class MemberController {
    Logger logger = LoggerFactory.getLogger(MemberController.class);

    @Autowired
    MemberService memberService;

    @ApiOperation(value="사용자아이디중복 확인", response= MemberIdDupCheckDTO.class)
    @GetMapping(value="/dupcheck-id/{id}")
    public MemberIdDupCheckDTO memberIdDupCheck(@ApiParam(value="사용자아이디", required=true) @PathVariable("id") String id) {
        return memberService.memberIdDupCheck(id);
    }

    @ApiOperation(value="회원가입", response= MemberSignUpDto.class)
    @PostMapping(value="/sign-up")
    public ResponseEntity<Void> memberIdDupCheck(@ApiParam(value="회원가입정보", required=true) @RequestBody MemberSignUpDto member) {
        memberService.signUp(member);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody MemberLoginDTO member) {
        return memberService.login(member);
    }
}
