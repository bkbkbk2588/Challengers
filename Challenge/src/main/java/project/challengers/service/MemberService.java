package project.challengers.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import project.challengers.DTO.member.*;
import reactor.core.publisher.Mono;

public interface MemberService {
    DupCheckDto memberIdDupCheck(String id);
    DupCheckDto memberPhoneDupCheck(String phone);
    void signUp(MemberDto member);
    Mono<ResponseEntity<LoginResponseDto>> login(LoginDto member);
    FindIdDto findId(String name, String phone);
    MemberDto findMember(Authentication authentication);
    int resetPassword(Authentication authentication);
    int updateMember(UpdateMemberDto member, Authentication authentication);
    int deleteMember(Authentication authentication);
}
