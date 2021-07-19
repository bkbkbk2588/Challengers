package project.challengers.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import project.challengers.DTO.member.*;

public interface MemberService {
    MemberDupCheckDTO memberIdDupCheck(String id);
    MemberDupCheckDTO memberPhoneDupCheck(String phone);
    void signUp(MemberDto member);
    ResponseEntity<LoginResponseDTO> login(MemberLoginDTO member);
    FindIdDTO findId(String name, String phone);
    MemberDto findMember(Authentication authentication);
}
