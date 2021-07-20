package project.challengers.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import project.challengers.DTO.member.*;
import project.challengers.service.MemberService;

@Api(tags = {"사용자"})
@RestController
@RequestMapping("/member")
public class MemberController {
    Logger logger = LoggerFactory.getLogger(MemberController.class);

    @Autowired
    MemberService memberService;

    @ApiOperation(value = "사용자아이디중복 확인", response = DupCheckDTO.class)
    @GetMapping(value = "/dupcheck-id/{id}")
    public DupCheckDTO memberIdDupCheck(@ApiParam(value = "사용자아이디", required = true) @PathVariable("id") String id) {
        return memberService.memberIdDupCheck(id);
    }

    @ApiOperation(value = "핸드폰번호 중복 확인", response = DupCheckDTO.class)
    @GetMapping(value = "/dupcheck-phone/{phone}")
    public DupCheckDTO memberPhoneDupCheck(@ApiParam(value = "핸드폰번호", required = true) @PathVariable("phone") String phone) {
        return memberService.memberPhoneDupCheck(phone);
    }

    @ApiOperation(value = "회원가입", response = MemberDto.class)
    @PostMapping(value = "/sign-up")
    public ResponseEntity<Void> memberIdDupCheck(@ApiParam(value = "회원가입정보", required = true) @RequestBody MemberDto member) {
        memberService.signUp(member);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation(value = "로그인", response = LoginResponseDTO.class)
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO member) {
        return memberService.login(member);
    }

    @ApiOperation(value = "아이디 찾기")
    @GetMapping("/findId/{name}/{phone}")
    public FindIdDTO findId(@ApiParam(value = "이름", required = true) @PathVariable("name") String name,
                            @ApiParam(value = "핸드폰번호", required = true) @PathVariable("phone") String phone) {
        return memberService.findId(name, phone);
    }

    @ApiOperation(value = "내정보 보기")
    @GetMapping("/findMember")
    public MemberDto findMember(Authentication authentication) {
        return memberService.findMember(authentication);
    }

    @ApiOperation(value = "비밀번호 찾기(초기화)")
    @GetMapping("/findPw/{id}/{name}/{phone}")
    public int findPw(@ApiParam("사용자ID") @PathVariable("id") String id,
                            @ApiParam("사용자이름") @PathVariable("name") String name,
                            @ApiParam("연락처") @PathVariable("phone") String phone) {
        memberService.resetPassword(id, name, phone);
        return 1;
    }
}
