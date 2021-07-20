package project.challengers.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import project.challengers.DTO.member.*;

public interface MemberService {
    DupCheckDTO memberIdDupCheck(String id);
    DupCheckDTO memberPhoneDupCheck(String phone);
    void signUp(MemberDto member);
    ResponseEntity<LoginResponseDTO> login(LoginDTO member);
    FindIdDTO findId(String name, String phone);
    MemberDto findMember(Authentication authentication);
    void resetPassword(String id, String name, String phone);
}
