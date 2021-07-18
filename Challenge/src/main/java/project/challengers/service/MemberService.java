package project.challengers.service;

import org.springframework.http.ResponseEntity;
import project.challengers.DTO.member.LoginResponseDTO;
import project.challengers.DTO.member.MemberIdDupCheckDTO;
import project.challengers.DTO.member.MemberLoginDTO;
import project.challengers.DTO.member.MemberSignUpDto;

public interface MemberService {
    public MemberIdDupCheckDTO memberIdDupCheck(String id);
    public void signUp(MemberSignUpDto member);
    public ResponseEntity<LoginResponseDTO> login(MemberLoginDTO member);
}
