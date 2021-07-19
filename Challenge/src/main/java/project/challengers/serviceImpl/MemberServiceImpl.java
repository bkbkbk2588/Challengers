package project.challengers.serviceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.challengers.DTO.member.*;
import project.challengers.component.JWTTokenComp;
import project.challengers.entity.Member;
import project.challengers.exception.ChallengersException;
import project.challengers.repository.MemberRepository;
import project.challengers.service.MemberService;

import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {
    Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTTokenComp jwtComp;

    /**
     * 아이디 중복확인
     *
     * @param id
     * @return
     */
    @Override
    public MemberDupCheckDTO memberIdDupCheck(String id) {
        String idDup = memberRepository.findByMemberId(id);

        if (idDup == null) {
            return MemberDupCheckDTO.builder()
                    .dupYn("N")
                    .build();
        }
        return MemberDupCheckDTO.builder()
                .dupYn("Y")
                .build();
    }

    /**
     * 핸드폰번호 중복확인
     * 계정 1개씩만 생성 가능하게 하기위해 핸드폰 중복확인
     * @param phone
     * @return
     */
    @Override
    public MemberDupCheckDTO memberPhoneDupCheck(String phone) {
        String idDup = memberRepository.findByMemberPhone(phone);

        if (idDup == null) {
            return MemberDupCheckDTO.builder()
                    .dupYn("N")
                    .build();
        }
        return MemberDupCheckDTO.builder()
                .dupYn("Y")
                .build();
    }

    /**
     * 회원가입
     *
     * @param member
     */
    @Override
    public void signUp(MemberDto member) {
        memberRepository.save(Member.builder()
                .id(member.getId())
                .pw(passwordEncoder.encode(member.getPw()))
                .name(member.getName())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .phone(member.getPhone())
                .build());
    }

    /**
     * 로그인
     *
     * @param member
     * @return
     */
    @Override
    public ResponseEntity<LoginResponseDTO> login(MemberLoginDTO member) {
        Member memberEntity = memberRepository.findById(member.getId())
                .orElseThrow(() -> new ChallengersException(HttpStatus.BAD_REQUEST,
                        "error.user.notfound.user.valid.E0001"));

        if (!passwordEncoder.matches(member.getPw(), memberEntity.getPw())) {
            throw new ChallengersException(HttpStatus.UNAUTHORIZED,
                    "error.user.login.fail.userpw.E0002");
        }

        return ResponseEntity.ok(LoginResponseDTO.builder()
                .accessToken(jwtComp.createToken(memberEntity.getId(), memberEntity.getRoles()))
                .member(LoginResponseDTO.Member.builder()
                        .id(memberEntity.getId())
                        .name(memberEntity.getName())
                        .nickname(memberEntity.getNickname())
                        .email(memberEntity.getEmail())
                        .phone(memberEntity.getPhone())
                        .build())
                .build());
    }

    /**
     * 아이디 찾기
     *
     * @param name
     * @param phone
     * @return
     */
    @Override
    public FindIdDTO findId(String name, String phone) {
        return FindIdDTO.builder()
                .id(memberRepository.findByNameAndPhone(name, phone))
                .build();
    }

    @Override
    public MemberDto findMember(Authentication authentication) {
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        Member member = memberRepository.findById((String) userDetails.getUsername()).get();

        return MemberDto.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .phone(member.getPhone())
                .build();
    }

    // TODO
    //  1. 비밀번호 찾기 (비밀번호 찾기 완료와 동시에 초기화 -> 초기화 규칙은 아이디앞3자리 + 휴대전화 뒷 4자리 + !)
    //  2. 회원정보 수정
    //  3. 회원 탈퇴
}
